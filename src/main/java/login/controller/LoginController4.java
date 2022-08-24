package login.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.service.MemberService;
import login.service.MemberServiceInter;

@WebServlet("/login/*")
public class LoginController4 extends HttpServlet{
	
	MemberServiceInter service = new MemberService();
	
	HashMap<String,String> map=new HashMap<String,String>();
	
	@Override
	public void init() throws ServletException {
		map.put("login","/login/login.jsp"); 
		map.put("member","/login/createMember.jsp");
		map.put("addlogin","/home/main.jsp");
		map.put("loginProc","/home/main.jsp");
		map.put("logout","/home/main.jsp");
	}
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] uris=req.getRequestURI().split("/");
		String page="/home/main.jsp";
		
		if(uris.length==2) {
			page=map.get("login");
		}else {
			page=map.get(uris[2]);
			
			if(uris[2].equals("loginProc")) {
	
				boolean result=service.login(req,resp);
				if(!result) page="/login/login.jsp";
				
			}else if(uris[2].equals("logout")) {	
				service.logout(req, resp);
				
			}else if(uris[2].equals("addlogin")) {
				service.addlogin(req,resp);
			}
		}
		
		if(page==null) page="/home/main.jsp";
		req.setAttribute("page",page.substring(1));
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
		
	}
}
