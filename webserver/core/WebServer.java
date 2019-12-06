package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer主类
 * @author ta
 *
 */
public class WebServer {
	private ServerSocket server;
	/**
	 * 構造方法，用來初始化伺服器
	 */
	public WebServer() {
		try {
			System.out.println("正在啟動服務端...");
			server = new ServerSocket(8088);
			System.out.println("服務端啟動完畢!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 服務端開始工作的方法
	 */
	public void start() {
		try {
			while(true) {
				System.out.println("等待客戶端連接...");
				Socket socket = server.accept();
				System.out.println("一個客戶端連接了！");
				//啟動一個線程處理客戶端請求
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}










