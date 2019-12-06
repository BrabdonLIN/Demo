package com.webserver.servlets;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ����Servlet�ĳ��
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request, HttpResponse response);

	/**
	 * ���D��ָ��·�� ע:TOMCAT�Ќ��Hԓ��������D�l�������� ͨ�^request�@ȡ��
	 */
	public void forward(String path, HttpRequest request, HttpResponse response) {
		response.setEntity(new File("webapps" + path));
	}

}
