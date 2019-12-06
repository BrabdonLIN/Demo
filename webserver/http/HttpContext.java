package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Http�f�h���P���ݶ��x
 *
 */
public class HttpContext {
	/**
	 * ��܇��CR
	 */
	public static final int CR = 13;
	/**
	 * �Q�з�LF
	 */
	public static final int LF = 10;

	/*
	 * ��B���a�c������B���� key:��B���a value:��B����
	 */
	private static Map<Integer, String> status_code_reason_mapping = new HashMap<Integer, String>();

	/*
	 * ���|���ӳ�� key:�YԴ��Y�� value:���|���(Content-Type������ֵ)
	 */
	private static Map<String, String> mime_mapping = new HashMap<String, String>();

	static {
		// ��ʼ���o�B�ɆT
		initStatusMapping();
		initMimeMapping();
	}

	/**
	 * ��ʼ�����|���
	 */
	private static void initMimeMapping() {
		// mime_mapping.put("html", "text/html");
		// mime_mapping.put("png", "image/png");
		// mime_mapping.put("gif", "image/gif");
		// mime_mapping.put("jpg", "image/jpeg");
		// mime_mapping.put("css", "text/css");
		// mime_mapping.put("js", "application/javascript");
		
		/*
		 * ����conf/web.xml�ļ��������˻`���� ����<mime-mapping>���Ә˻`��ȡ�����K
		 * ��ԓ�˻`�е��Ә˻`<extension>���g���ı� ����key���Ә˻`<mime-type>���g���ı�����
		 * value���浽mime_mapping�@��Map����� ��ʼ������
		 */
		
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			for (Element mime : mimeList) {
				String ext = mime.elementText("extension");
				String type = mime.elementText("mime-type");
				mime_mapping.put(ext, type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��ʼ����B���a�c��������
	 */
	private static void initStatusMapping() {
		status_code_reason_mapping.put(200, "OK");
		status_code_reason_mapping.put(201, "Created");
		status_code_reason_mapping.put(202, "Accepted");
		status_code_reason_mapping.put(204, "No Content");
		status_code_reason_mapping.put(301, "Moved Permanently");
		status_code_reason_mapping.put(302, "Moved Temporarily");
		status_code_reason_mapping.put(304, "Not Modified");
		status_code_reason_mapping.put(400, "Bad Request");
		status_code_reason_mapping.put(401, "Unauthorized");
		status_code_reason_mapping.put(403, "Forbidden");
		status_code_reason_mapping.put(404, "Not Found");
		status_code_reason_mapping.put(500, "Internal Server Error");
		status_code_reason_mapping.put(501, "Not Implemented");
		status_code_reason_mapping.put(502, "Bad Gateway");
		status_code_reason_mapping.put(503, "Service Unavailable");
	}

	/**
	 * ������B���a�@ȡ�����Ġ�B����
	 */
	public static String getStatusReason(int code) {
		return status_code_reason_mapping.get(code);
	}

	/**
	 * �����YԴ��Y�@ȡ�����Ľ��|���
	 */
	public static String getMimeType(String ext) {
		return mime_mapping.get(ext);
	}

	public static void main(String[] args) {
		String type = getMimeType("png");
		System.out.println(type);
	}
}
