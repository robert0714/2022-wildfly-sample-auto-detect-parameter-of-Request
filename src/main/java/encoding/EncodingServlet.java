package encoding;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = { "/postData" })
public class EncodingServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attr01 = request.getParameter("att01");
//		String str = new String(attr01.getBytes("iso-8859-1"),"utf-8");
//		String str = new String(attr01.getBytes("iso-8859-1"),"big5");
		System.out.println("--------------------: ");
		System.out.println("中文 ");
		System.out.println("atto1: " + attr01);
//		System.out.println("atto1 after converting : " + str);
		System.out.println("--------------------: ");
	}
}
