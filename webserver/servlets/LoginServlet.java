package com.webserver.servlets;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 登錄業務
 */
public class LoginServlet extends HttpServlet {
	public void service(HttpRequest request, HttpResponse response) {
		// 1 獲取用戶登錄訊息
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// 2讀user.dat文件，比對用戶​​訊息
		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "r");) {

			// 開關，默認登錄失敗
			boolean check = false;
			for (int i = 0; i < raf.length() / 100; i++) {

				// 移動指針到當前記錄的開始位置
				raf.seek(i * 100);

				// 讀用戶名
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data, "UTF-8").trim();
				if (name.equals(username)) {

					// 讀密碼
					raf.read(data);
					String pwd = new String(data, "UTF-8").trim();
					if (pwd.equals(password)) {

						// 登錄成功
						check = true;
					}
					break;
				}
			}

			if (check) {

				// 登錄成功
				forward("/myweb/login_success.html", request, response);
			} else {

				// 登錄失敗
				forward("/myweb/login_fail.html", request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
