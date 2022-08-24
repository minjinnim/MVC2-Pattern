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
public class LoginController extends HttpServlet{
	//처리할내용이 있기때문에 service를 생성해줌
	MemberServiceInter service = new MemberService();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] uris=req.getRequestURI().split("/");
		
		String page="login/";
		
		int timeout=2*60;
		
		//localhost:8080/login 으로 받았을때
		if(uris.length==2) {
			//req.setAttribute("page",page+"login.jsp");
			
			req.setAttribute("page","login/login.jsp");
			req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
		
		}else {
			if(uris[2].equals("login")) {
				//localhost:8080/login/login 으로 받았을때
				
				page+="login.jsp";
				req.setAttribute("page",page);
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
			
			}else if(uris[2].equals("member")) {
				//localhost:8080/login/member 으로 받았을때
				page+="createMember.jsp";
				req.setAttribute("page",page);
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
			
			}else if(uris[2].equals("loginProc")) {
				//localhost:8080/login/loginProc 으로 받았을때
				
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
					System.out.println("접속시간:"+req.getSession().getCreationTime());
					//접속시간을 계산해서 알기쉽게 바꾸는 것
					SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
					System.out.println("접속시간:"+fmt.format(req.getSession().getCreationTime()));
					
					
					//page+="loginSuccess.jsp";
					//loginSuccess.jsp파일에서 alert해주고 forward를 사용해 main.jsp로 보내주면됨
					page="/home/main.jsp";
					req.setAttribute("page",page);
					
				}else { //로그인 실패했을 경우
					page+="loginFail.jsp";
					req.setAttribute("page",page);
				}
				
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
				
			}else if(uris[2].equals("logout")) {
				//localhost:8080/login/logout 으로 받았을때
				
				req.getSession().invalidate(); //세션을 해제시켜주는 코드, 바로 사용하는 방법이 있음
				
				//로그아웃한 시간
				System.out.println("접속해제시간:"+req.getSession().getLastAccessedTime());
				//접속시간을 계산해서 알기쉽게 바꾸는 것
				SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
				System.out.println("접속해제시간:"+fmt.format(req.getSession().getLastAccessedTime()));
				
				//service.logout(req,resp); //서비스에서 처리하게 만들어주는것, 이 함수안에 위에 코드를 추가해서 사용하는 방법도 있음
				page="/home/main.jsp";
				req.setAttribute("page",page);
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
				
			}else if(uris[2].equals("addlogin")) {
				//localhost:8080/login/addlogin 으로 받았을때
				
				req.getSession().setMaxInactiveInterval(timeout);
				req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
				
				page="/home/main.jsp";
				req.setAttribute("page",page);
				req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
			}
			
			
			
		}
	}
}
