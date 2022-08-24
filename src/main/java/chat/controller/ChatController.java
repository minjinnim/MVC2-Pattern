package chat.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/chat/*")
public class ChatController extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] uris=req.getRequestURI().split("/");
		String page="chat/";
		
		if(uris.length==2) {
			page+="chat.jsp";
		}else {
			if(uris[2].equals("chat")) {
				page+="chat.jsp";
			}
		}
		
		req.setAttribute("page",page);
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
	}
}
