package board.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.vo.BoardVO;
import board.vo.PageList;
import login.vo.MemberVO;

public interface BoardServiceInter {
	PageList pageList(HttpServletRequest req, HttpServletResponse resp);
	BoardVO findOne(HttpServletRequest req, HttpServletResponse resp);
	MemberVO findOne(String id);
	int insert(HttpServletRequest req, HttpServletResponse resp);
	int update(HttpServletRequest req, HttpServletResponse resp);
	int delete(HttpServletRequest req, HttpServletResponse resp);
	BoardVO replyInfo(HttpServletRequest req, HttpServletResponse resp);
	int replyInsert(HttpServletRequest req, HttpServletResponse resp);
	 
}

