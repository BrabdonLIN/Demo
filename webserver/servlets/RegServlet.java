package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * ̎���]�ԘI��
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {

/*
* �]�Դ�������:
* 1:�@ȡ�Ñ��ύ���]����Ϣ
* 2:���]����Ϣ�����ļ�user.dat
* 3:푑��͑����]�Գɹ������
*/
		System.out.println("�_ʼ̎���]�ԘI��!!!");
		/*
		* 1
		* ͨ�^request.getParameter()�����@ȡ�Ñ�
		* �ύ�ρ�Ĕ����r�����f�ą����@���ַ�����
		* ֵ�����������form����e������ݔ����
		* ����(name���Ե�ֵ)
		*/
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(request.getParameter("age"));
		System.out.println("username:"+username);
		System.out.println("password:"+password);
		System.out.println("nickname:"+nickname);
		System.out.println("age:"+age);

/*
* 2
* ÿ�lӛ䛁�100�ֹ��������Ñ������ܴa
* ���Q���ַ���������32�ֹ���
* ���g��intֵ��4�ֹ������뵽user.dat
* �ļ���
*/
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw");	
		){	

			//�F��ָ��Ƅӵ��ļ�ĩβ
			raf.seek(raf.length());		

			//���Ñ���
			//1�Ȍ��Ñ����D�Ɍ�����һ�M�ֹ�
			byte[] data = username.getBytes("UTF-8");

			//2��ԓ���M�U�ݞ�32�ֹ�
			data = Arrays.copyOf(data, 32);

			//3��ԓ�ֹ����Mһ���Ԍ����ļ�
			raf.write(data);			
			//���ܴa
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);			
			//�����Q
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);			
			//�����g
			raf.writeInt(age);
			System.out.println("�]���ꮅ!");
			
			//3푑��͑����]�Գɹ����
			forward("/myweb/reg_success.html", request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}



