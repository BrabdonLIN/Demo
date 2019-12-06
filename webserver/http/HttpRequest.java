package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.webserver.core.EmptyRequestException;

/**
 * Ո����
 * ÿ��������ʾ�͑��˰l���^���һ�����wՈ��
 */
public class HttpRequest {
	/*
	 * Ո�������PӍϢ���x
	 */
	//Ո��ʽ
	private String method;
	//�YԴ·��
	private String url;
	//�f�h�汾
	private String protocol;
	
	//url�е�Ո�󲿷�
	private String requestURI;
	//url�еą�������
	private String queryString;
	//ÿ������
	private Map<String,String> parameters = new HashMap<String,String>();
	/*
	 * ��Ϣ�^���PӍϢ���x
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	/*
	 * ��Ϣ�������PӍϢ���x
	 */
	//�͑����B�����PӍϢ
	private Socket socket;
	private InputStream in;
	
	/**
	 * ��ʼ��Ո��
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			this.in = socket.getInputStream();
			/*
			 * ��������
			 * 1:����������
			 * 2:������Ϣͷ
			 * 3:������Ϣ����
			 */
			parseRequestLine();
			parseHeaders();
			parseContent();
			
		} catch(EmptyRequestException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����������
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("�_ʼ����Ո����...");	
		try {
			String line = readLine();
			System.out.println("Ո����:"+line);
			/*
			 * ��Ո�����M�в�֣���ÿ���փ���
			 * �������O�õ������ϡ�
			 */
			String[] data = line.split("\\s");
			if(data.length!=3) {
				//��Ո��
				throw new EmptyRequestException();
			}
			method = data[0];
			url = data[1];
			//�Mһ������URL
			parseURL();
			protocol = data[2];
			
			System.out.println("method:"+method);
			System.out.println("url:"+url);
			System.out.println("protocol:"+protocol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Ո���н����ꮅ!");
	}
	/**
	* �Mһ������URL
	* url�п��ܕ��ЃɷN��ʽ:�������Ͳ�������
	* 1,����������:
	* /myweb/index.html
	*
	* 2,��������:
	* /myweb/reg?username=zhangsan&password=123456&nickname=asan&age=22
	*/
	private void parseURL() {
		/*
		* �����Дஔǰurl�Ƿ��Ѕ���,�Д��
		* �����ǿ�url�Ƿ���"?",���Єt�J��
		* �@��url�ǰ��������ģ���tֱ�ӌ�url
		* �xֵ�orequestURI���ɡ�
		*
		*
		* ���Ѕ���:
		* 1:��url����"?"��֞�ɲ��֣���һ����
		* ��Ո�󲿷֣��xֵ�orequestURI
		* �ڶ����֞酢�����֣��xֵ�oqueryString
		*
		* 2:�ٌ�queryString�Mһ����֣��Ȱ���"&"
		* ��ֳ�ÿ���������ٌ�ÿ����������"="
		* ��֞酢�����c����ֵ���K����parameters
		* �@��Map�С�
		*
		* �����^����Ҫע��url�Ďׂ��؄e��r:
		* 1:url���ܺ���"?"���Ǜ]�Ѕ�������
		* ��:
		* /myweb/reg?
		*
		* 2:���������п���ֻ�Ѕ������]�Ѕ���ֵ
		* ��:
		* /myweb/reg?username=&password=123&age=16...
		*/
		if(url.indexOf("?")!=-1) {
			//����"?"���
			String[] data = url.split("\\?");
			requestURI = data[0];
			//�Д�?�����Ƿ��Ѕ���
			if(data.length>1) {
				queryString = data[1];
				//�Mһ��������������
				parseParameter(queryString);
			}
		}else {
			//������?
			requestURI = url;
		}
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
	}
	/**
	 * ������Ϣ�^
	 */
	private void parseHeaders() {
		System.out.println("�_ʼ������Ϣ�^...");
		try {
			/*
			* ������Ϣ�^������:
			* ѭ�h�{��readLine�������xȡÿһ����Ϣ�^
			* ��readLine��������ֵ����ַ����rֹͣ
			* ѭ�h(��鷵�ؿ��ַ����f���Ϊ��xȡ��CRLF
			* ���@��������Ϣ�^�Y���Ę��I)
			* ���xȡ��ÿ����Ϣ�^�ᣬ����": "(ð̖�ո�)
			* �M�в�֣��K����Ϣ�^����������key����Ϣ
			* �^������ֵ����value���浽����headers�@��
			* Map����ɽ�������
			*/
			while(true) {
				String line = readLine();
				if("".equals(line)) {
					break;
				}
				String[] data = line.split(":\\s");
				headers.put(data[0], data[1]);
			}
			System.out.println("headers:"+headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("��Ϣ�^�����ꮅ!");
	}
	/**
	 * ������Ϣ����
	 */
	private void parseContent() {
		System.out.println("�_ʼ������Ϣ����...");
		/*
		* ������Ϣ�^�Ƿ���Content-Length�Q��
		* ԓՈ���Ƿ�����Ϣ����
		*/
		try {
			if(headers.containsKey("Content-Length")) {
				//������Ϣ���ĵ�
				int length = Integer.parseInt(
					headers.get("Content-Length")	
				);
				//�xȡ��Ϣ���ă���
				byte[] data = new byte[length];
				in.read(data);
				/*
				* ������Ϣ�^Content-Type�Д�ԓ
				* ��Ϣ���ĵĔ������
				*/
				String contentType 
					= headers.get("Content-Type");
				//�Д��Ƿ��form����ύ����
				if("application/x-www-form-urlencoded".equals(contentType)) {
					/*
					* ԓ���ă����ஔ�ԭGETՈ���ַ���e
					* url�С�?���҂ȃ���
					*/
					String line = new String(data,"ISO8859-1");
					System.out.println("��������:"+line);
					parseParameter(line);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("��Ϣ���Ľ������!");
	}
	/**
	 * ��������
	 * ��ʽ:name=value&name=value&...
	 * @param line
	 */
	private void parseParameter(String line) {
		/*
		 * �ֽ������е�"%XX"�����ݰ��ն�Ӧ
		 * �ַ���(�����ͨ����UTF-8)��ԭΪ
		 * ��Ӧ����
		 */
		try {
			/*
			 * URLDecoder��decode�������Խ�������
			 * �ַ����е�"%XX"����תΪ��Ӧ2�����ֽ�
			 * Ȼ���ո������ַ�������Щ�ֽڻ�ԭ
			 * Ϊ��Ӧ�ַ����滻��Щ"%XX"���֣�Ȼ��
			 * �����õ��ַ�������
			 * ����line������Ϊ:
			 * username=%E8%8C%83%E4%BC%A0%E5%A5%87&password=123456
			 * ת����Ϻ�Ϊ:
			 * username=������&password=123456
			 * 
			 */
			System.out.println("�Բ���ת��ǰ:"+line);
			line = URLDecoder.decode(line, "UTF-8");
			System.out.println("�Բ���ת���:"+line);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		
		//����&��ֳ�ÿһ������
		String[] paraArr = line.split("&");
		//����ÿ���������в��
		for(String para : paraArr) {
			//�ٰ���"="���ÿ������
			String[] paras = para.split("=");
			if(paras.length>1) {
				//�ò�����ֵ
				parameters.put(paras[0], paras[1]);
			}else {
				//û��ֵ
				parameters.put(paras[0], null);
			}
		}
	}
	
	/**
	 * ��ȡһ���ַ�������������ȡCR,LFʱֹͣ
	 * ����֮ǰ��������һ���ַ�����ʽ���ء�
	 * @return
	 * @throws IOException
	 */
	private String readLine() throws IOException {
		StringBuilder builder = new StringBuilder();
		//���ζ�ȡ���ֽ�
		int d = -1;
		//c1��ʾ�ϴζ�ȡ���ַ���c2��ʾ���ζ�ȡ���ַ�
		char c1='a',c2='a';
		while((d = in.read())!=-1) {
			c2 = (char)d;
			if(c1==HttpContext.CR&&c2==HttpContext.LF) {
				break;
			}
			builder.append(c2);
			c1 = c2;
		}
		return builder.toString().trim();
		
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	/**
	 * ���ݸ�������Ϣͷ�����ֻ�ȡ��Ӧ��Ϣͷ��
	 * ֵ
	 * @param name
	 * @return
	 */
	public String getHeader(String name) {
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
}









