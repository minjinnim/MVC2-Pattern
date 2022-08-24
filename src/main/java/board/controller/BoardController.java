package board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import board.service.BoardService;
import board.service.BoardServiceInter;
import board.vo.BoardVO;
import board.vo.PageList;
import login.vo.MemberVO;

@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	BoardServiceInter service = new BoardService();

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String[] uris=req.getRequestURI().split("/");
		String page="board/";
	
		//로그인 되지 않은 경우 login 홈페이지 이동
		//그렇지 않을 경우 board관련 명령 처리
		if(req.getSession().getAttribute("id")==null) {
			page="/login/login.jsp";
		}else {
			if(uris[2].equals("list")) {
				req.setAttribute("sessiontime", req.getSession().getMaxInactiveInterval());
				PageList pagelist=service.pageList(req,resp);
				req.setAttribute("pagelist", pagelist);
				page+="pageList.jsp";
				
			}else if(uris[2].equals("findOne")) {
				BoardVO board = service.findOne(req,resp);
				req.setAttribute("board", board);
				page+="findOne.jsp";
			}else if(uris[2].equals("writeForm")) {
				/*
				String id=(String)req.getSession().getAttribute("id");
				MemberVO member = service.findOne(id);
				req.setAttribute("member", member);
				*/
				page+="writeForm.jsp";
			}else if(uris[2].equals("writeFormProc")) {
				int result=service.insert(req,resp);
				resp.sendRedirect("/board/list");
				return;
			}else if(uris[2].equals("update")) {
				int result=service.update(req,resp);
				resp.sendRedirect("/board/list");
				return;
			}else if(uris[2].equals("delete")) {
				int result=service.delete(req,resp);
				resp.sendRedirect("/board/list");
				return;
			}else if(uris[2].equals("replyForm")) {
				req.setAttribute("board", service.replyInfo(req, resp));
				page+="replyForm.jsp";
				
			}else if(uris[2].equals("replyFormProc")) {
				int result=service.replyInsert(req,resp);
				resp.sendRedirect("/board/list");
				return;
			}else {
				return;
			}
		}
		
		req.setAttribute("page",page);
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req,resp);
	}

}
