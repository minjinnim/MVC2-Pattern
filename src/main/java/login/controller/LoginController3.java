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

//@WebServlet("/login/*")
public class LoginController3 extends HttpServlet{
	MemberServiceInter service = new MemberService();
	
	HashMap<String,String> map=new HashMap<String,String>();
	
	int timeout=2*60;
	
	@Override
	public void init() throws ServletException {
		//뒤에는 정확한 경로를 적어놔야함
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
				
				if(result==true) {
					req.getSession().setAttribute("id", req.getParameter("id"));
					
					req.getSession().setMaxInactiveInterval(timeout); 
					req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
					
					System.out.println("접속한 ip:"+req.getRemoteAddr());
					SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
					System.out.println("접속시간:"+fmt.format(req.getSession().getCreationTime()));
					
				}else {
					//?
					page="/login/login.jsp";
				}
				
			}else if(uris[2].equals("logout")) {	
				req.getSession().invalidate(); 
	
				SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
				System.out.println("접속해제시간:"+fmt.format(req.getSession().getLastAccessedTime()));

				
			}else if(uris[2].equals("addlogin")) {
				
				req.getSession().setMaxInactiveInterval(timeout);
				req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
				
				page="/home/main.jsp";
			}
		}
		
		if(page==null) page="/home/main.jsp";
		req.setAttribute("page",page.substring(1));
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
	}
}
