package login.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.service.MemberService;
import login.service.MemberServiceInter;

//@WebServlet("/login/*")
public class LoginController2 extends HttpServlet{
	//처리할내용이 있기때문에 service를 생성해줌
	MemberServiceInter service = new MemberService();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] uris=req.getRequestURI().split("/");
		
		String page="login/";
		
		int timeout=2*60;

		if(uris.length==2) {
			page+="login.jsp";
		}else {
			if(uris[2].equals("login")) {
				page+="login.jsp";
			}else if(uris[2].equals("member")) {
				page+="createMember.jsp";
			}else if(uris[2].equals("loginProc")) {
	
				boolean result=service.login(req,resp);
				
				if(result==true) {
					//로그인 성공시 세션 발급
					req.getSession().setAttribute("id", req.getParameter("id"));
					
					//세션시간설정
					req.getSession().setMaxInactiveInterval(timeout); //초가 지나면 세션 해제
					
					//세션시간 header.jsp로 전달 
					req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
					
					//ip, ip로 로그인한 시간
					System.out.println("접속한 ip:"+req.getRemoteAddr());
					//접속시간을 계산해서 알기쉽게 바꾸는 것
					SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
					System.out.println("접속시간:"+fmt.format(req.getSession().getCreationTime()));
				
					page="/home/main.jsp";
					
				}else { //로그인 실패했을 경우
					page+="loginFail.jsp";
				}
				
			}else if(uris[2].equals("logout")) {	
				req.getSession().invalidate(); //세션을 해제시켜주는 코드
	
				SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
				System.out.println("접속해제시간:"+fmt.format(req.getSession().getLastAccessedTime()));

				page="/home/main.jsp";
				
			}else if(uris[2].equals("addlogin")) {
				
				req.getSession().setMaxInactiveInterval(timeout);
				req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
				
				page="/home/main.jsp";
			}
			
		}
		
		req.setAttribute("page",page);
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
	}
}
