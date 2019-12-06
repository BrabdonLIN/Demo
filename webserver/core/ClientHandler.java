package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlets.HttpServlet;
import com.webserver.servlets.LoginServlet;
import com.webserver.servlets.RegServlet;
import com.webserver.servlets.UpdateServlet;

/**
 * 處理客戶端請求
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		try {
			/*
			 * 主流程:
			 * 1:解析請求
			 * 2:處理請求
			 * 3:發送響應
			 */
			//1準備工作
			//1.1解析請求，創建請求對象
			HttpRequest request = new HttpRequest(socket);
			//1.2創建響應對象
			HttpResponse response = new HttpResponse(socket);
			//2處理請求
			//2.1:獲取請求的路徑資源
			String url = request.getRequestURI();
			//判斷該請求是否為業務需求
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null) {
				System.out.println("ClientHandler:正在載入"+servletName);
				Class cls = Class.forName(servletName);
				HttpServlet servlet 
					= (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else {				
				//2.2:根據資源路徑去webapps目錄中尋找資源
				File file = new File("webapps"+url);
				if(file.exists()) {
					System.out.println("找到該資源!");
					//向響應對象中設置要響應的資源內容				
					response.setEntity(file);
				}else {
					//設置狀態代碼404
					response.setStatusCode(404);
					//設置404頁面
					response.setEntity(
						new File("webapps/root/404.html")
					);				
					System.out.println("資源不存在!");			
				}
			}
			//3響應客戶端
			response.flush();
			//http://localhost:8088/myweb/index.html	
		} catch(EmptyRequestException e) {
			/*
			 * 實例化HttpRequest時若發現是空請求時
			 * 該構造造方法將會拋出異常，這裡不做任何處理直接在finally中與客戶端斷開即可
			 */
			System.out.println("空請求!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//與客戶端斷開連結
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	

}





