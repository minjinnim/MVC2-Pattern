package login.service;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import login.dao.MemberDAO;
import login.dao.MemberDAOInter;
import login.vo.MemberVO;

public class MemberService implements MemberServiceInter{
	MemberDAOInter dao = new MemberDAO();
	
	int timeout=2*60;
	
	@Override
	public boolean login(HttpServletRequest req, HttpServletResponse resp) {
		
		String id=req.getParameter("id");
		String password=req.getParameter("password");
		MemberVO member = new MemberVO(id,password);
		boolean result= dao.login(member);
		
		if(result==true) {
			req.getSession().setAttribute("id", req.getParameter("id"));
			
			req.getSession().setMaxInactiveInterval(timeout); 
			req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
			
			System.out.println("접속한 ip:"+req.getRemoteAddr());
			SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			System.out.println("접속시간:"+fmt.format(req.getSession().getCreationTime()));
			
		}
		
		return result;
	}
	
	@Override
	public void logout(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().invalidate(); 
		
		SimpleDateFormat fmt= new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		System.out.println("접속해제시간:"+fmt.format(req.getSession().getLastAccessedTime()));
		
	}
	
	@Override
	public void addlogin(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().setMaxInactiveInterval(timeout);
		req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
		
	}
	
}
