package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 푑����� ԓ�ÿһ��������춱�ʾһ�����wҪ�o�͑��� 푑��ă��� һ��푑�����: ��B�У�푑��^��푑�����
 */
public class HttpResponse {
	/*
	 * ��B�����P��Ϣ���x
	 */
	// ��B���a
	private int statusCode = 200;
	// ��B����
	private String statusReason = "OK";

	/*
	 * 푑��^���P��Ϣ���x
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/*
	 * 푑��������P��Ϣ���x
	 */
	// 푑��Č��w�ļ�
	private File entity;

	// 푑��Č��w�ļ�
	private Socket socket;
	private OutputStream out;

	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ǰ푑����ݰl�ͽo�͑���
	 */
	public void flush() {

		/*
		 * 푑��͑���: 1:�l�͠�B�� 2:�l��푑��^ 3:�l��푑�����
		 */
		try {
			sendStatusLine();
			sendHeaders();
			sendContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �l�͠�B��
	 */
	private void sendStatusLine() {
		try {
			String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �l��푑��^
	 */
	private void sendHeaders() {
		try {

			// ��vheaders��������푑��^���
			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> header : set) {
				String key = header.getKey();
				String value = header.getValue();
				String line = key + ": " + value;
				println(line);
			}

			// �Ϊ��l��CRLF����ʾ푑��^���ֽY��
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �l��푑�����
	 */
	private void sendContent() {
		try (FileInputStream fis = new FileInputStream(entity);) {
			byte[] data = new byte[1024 * 10];
			int len = -1;
			while ((len = fis.read(data)) != -1) {
				out.write(data, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getEntity() {
		return entity;
	}

	/**
	 * �O��푑����w�ļ������O�õ�ͬ�r�������ļ���� �Ԅ���ӌ�����Content-Type�cContent-Length �@�ɂ�푑��^��
	 */
	public void setEntity(File entity) {
		this.entity = entity;

		// �����o�����ļ��Ԅ��O�Ì�����Content-Type�cContent-Length
		this.headers.put("Content-Length", entity.length() + "");
		// �@ȡ�YԴ��Y����ȥHttpContext�Ы@ȡ�����Ľ��|���
		// �@ȡ�YԴ�ļ���
		String fileName = entity.getName();
		int index = fileName.lastIndexOf(".") + 1;
		String ext = fileName.substring(index);
		String contentType = HttpContext.getMimeType(ext);
		this.headers.put("Content-Type", contentType);
	}

	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * �O�à�B���a���O������Ԅӌ����������� �O�ú�
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		this.statusReason = HttpContext.getStatusReason(statusCode);
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * ���ָ����푑��^��Ϣ
	 *
	 * @param name
	 *            푑��^������
	 * @param value
	 *            푑��^������ֵ
	 */
	public void putHeader(String name, String value) {
		this.headers.put(name, value);
	}

	/**
	 * ��͑��˰l��һ���ַ��� �l������ԄӰl��CR,LF
	 */
	private void println(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);// written CR
			out.write(HttpContext.LF);// written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
