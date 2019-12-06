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
 * ̎��͑���Ո��
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	public void run() {
		try {
			/*
			 * ������:
			 * 1:����Ո��
			 * 2:̎��Ո��
			 * 3:�l��푑�
			 */
			//1�ʂ乤��
			//1.1����Ո�󣬄���Ո����
			HttpRequest request = new HttpRequest(socket);
			//1.2����푑�����
			HttpResponse response = new HttpResponse(socket);
			//2̎��Ո��
			//2.1:�@ȡՈ���·���YԴ
			String url = request.getRequestURI();
			//�Д�ԓՈ���Ƿ��I������
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null) {
				System.out.println("ClientHandler:�����d��"+servletName);
				Class cls = Class.forName(servletName);
				HttpServlet servlet 
					= (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else {				
				//2.2:�����YԴ·��ȥwebappsĿ��Ќ����YԴ
				File file = new File("webapps"+url);
				if(file.exists()) {
					System.out.println("�ҵ�ԓ�YԴ!");
					//��푑��������O��Ҫ푑����YԴ����				
					response.setEntity(file);
				}else {
					//�O�à�B���a404
					response.setStatusCode(404);
					//�O��404���
					response.setEntity(
						new File("webapps/root/404.html")
					);				
					System.out.println("�YԴ������!");			
				}
			}
			//3푑��͑���
			response.flush();
			//http://localhost:8088/myweb/index.html	
		} catch(EmptyRequestException e) {
			/*
			 * ������HttpRequest�r���l�F�ǿ�Ո��r
			 * ԓ�����췽�����������������@�e�����κ�̎��ֱ����finally���c�͑��˔��_����
			 */
			System.out.println("��Ո��!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//�c�͑��˔��_�B�Y
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	

}





