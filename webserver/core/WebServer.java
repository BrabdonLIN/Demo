package com.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer����
 * @author ta
 *
 */
public class WebServer {
	private ServerSocket server;
	/**
	 * ���췽�����Á��ʼ���ŷ���
	 */
	public WebServer() {
		try {
			System.out.println("���چ��ӷ��ն�...");
			server = new ServerSocket(8088);
			System.out.println("���նˆ����ꮅ!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ���ն��_ʼ�����ķ���
	 */
	public void start() {
		try {
			while(true) {
				System.out.println("�ȴ��͑����B��...");
				Socket socket = server.accept();
				System.out.println("һ���͑����B���ˣ�");
				//����һ������̎��͑���Ո��
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










