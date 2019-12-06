package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Http協議相關內容定義
 *
 */
public class HttpContext {
	/**
	 * 回車符CR
	 */
	public static final int CR = 13;
	/**
	 * 換行符LF
	 */
	public static final int LF = 10;

	/*
	 * 狀態代碼與對應狀態描述 key:狀態代碼 value:狀態描述
	 */
	private static Map<Integer, String> status_code_reason_mapping = new HashMap<Integer, String>();

	/*
	 * 介質類型映射 key:資源後綴名 value:介質類型(Content-Type對應的值)
	 */
	private static Map<String, String> mime_mapping = new HashMap<String, String>();

	static {
		// 初始化靜態成員
		initStatusMapping();
		initMimeMapping();
	}

	/**
	 * 初始化介質類型
	 */
	private static void initMimeMapping() {
		// mime_mapping.put("html", "text/html");
		// mime_mapping.put("png", "image/png");
		// mime_mapping.put("gif", "image/gif");
		// mime_mapping.put("jpg", "image/jpeg");
		// mime_mapping.put("css", "text/css");
		// mime_mapping.put("js", "application/javascript");
		
		/*
		 * 解析conf/web.xml文件，將跟標籤所有 名為<mime-mapping>的子標籤或取到，並
		 * 將該標籤中的子標籤<extension>中間的文本 作為key，子標籤<mime-type>中間的文本作為
		 * value保存到mime_mapping這個Map中完成 初始化工作
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
	 * 初始化狀態代碼與對應描述
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
	 * 根據狀態代碼獲取對應的狀態描述
	 */
	public static String getStatusReason(int code) {
		return status_code_reason_mapping.get(code);
	}

	/**
	 * 根據資源後綴獲取對應的介質類型
	 */
	public static String getMimeType(String ext) {
		return mime_mapping.get(ext);
	}

	public static void main(String[] args) {
		String type = getMimeType("png");
		System.out.println(type);
	}
}
