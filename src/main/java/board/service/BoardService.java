package board.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import board.dao.BoardDAO;
import board.dao.BoardDAOInter;
import board.vo.PageList;
import login.dao.MemberDAO;
import login.dao.MemberDAOInter;
import login.vo.MemberVO;
import board.vo.BoardVO;

public class BoardService implements BoardServiceInter{

	BoardDAOInter dao=new BoardDAO();
	MemberDAOInter memberdao = new MemberDAO();
	
	public PageList pageList(HttpServletRequest req, HttpServletResponse resp) {
		
		int totalCount=0;
		int countPerPage=10;
		int totalPage=0;
		int startPage=0;
		int endPage=0;
		int startRow=0;
		int endRow=0;
		int currentPage=1;
		
		List<BoardVO> list = null;
		
		PageList plist=new PageList();
		
		
		totalCount=dao.totalCount();
		
		totalPage=(totalCount/countPerPage)+1;
	
		if((totalCount%countPerPage)==0) 
		totalPage=(totalCount/countPerPage);
		
		
		String _currentPage=req.getParameter("currentPage");
		if(_currentPage==null) {
			currentPage=1;
		}else if(!_currentPage.equals("")) {
			currentPage=Integer.parseInt(_currentPage);
		}
		
		startRow=(currentPage-1)*countPerPage+1;
		endRow=startRow+countPerPage-1;
				
		list=dao.pageList(startRow, endRow, totalPage, currentPage, totalCount);
				
		if(currentPage<=5){
			startPage=1;	
		}else{
			if(currentPage%5==0){
				startPage=(currentPage/5)*5;	
			}else{
				startPage=(currentPage/5)*5+1; //--처리 10페이지일 때 문제 발생
			}
		}
		
		endPage=startPage+4;
		
		if(endPage>totalPage) endPage=totalPage;
				
		plist.setList(list);
		plist.setStartPage(startPage);
		plist.setEndPage(endPage);
		plist.setCurrentPage(currentPage);
		plist.setCountPerPage(countPerPage);
		plist.setTotalCount(totalCount);
		plist.setTotalPage(totalPage);
		/*
		 * 잘출력되는지확인
		for(BoardVO board:plist.getList()) {
			System.out.println(board);
		}
		*/
	
		
		return plist;
	}

	@Override
	public BoardVO findOne(HttpServletRequest req, HttpServletResponse resp) {
		int idx=Integer.parseInt(req.getParameter("idx"));
		dao.readcountUp(idx);
		BoardVO board=dao.findOne(idx);
		return board;
	}

	@Override
	public MemberVO findOne(String id) {
		return memberdao.findOne(id);
	}

	@Override
	public int insert(HttpServletRequest req, HttpServletResponse resp) {
		String savePath="C:\\project\\work\\jwork\\mvc2_responsive\\src\\main\\webapp\\file\\uploadFold";
		int fileSize=10*1024*1024;
		MultipartRequest multi=null;
		try {
			multi = new MultipartRequest(req,savePath,fileSize,"utf-8",new DefaultFileRenamePolicy());
			Enumeration files=multi.getFileNames();
			
			String file=(String)files.nextElement();
			
			String title=multi.getParameter("title");
			String content=multi.getParameter("content");
			String filename = multi.getFilesystemName(file);
			
			BoardVO board=new BoardVO();
			board.setTitle(title);
			board.setContent(content);
			board.setWriteId((String)req.getSession().getAttribute("id"));
			board.setWriteName((String)req.getSession().getAttribute("id"));
			board.setFilename(filename);
			dao.insert(board);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public int update(HttpServletRequest req, HttpServletResponse resp) {
		BoardVO board=new BoardVO();
		int idx=0;
		String title=null;
		String content=null;
		String filename=null;
		
		String savePath="C:\\project\\work\\jwork\\mvc2_responsive\\src\\main\\webapp\\file\\uploadFold";
		int fileSize=10*1024*1024;
		MultipartRequest multi=null;
		
		try {
			multi = new MultipartRequest(req,savePath,fileSize,"utf-8",new DefaultFileRenamePolicy());
			Enumeration files=multi.getFileNames();
			
			idx=Integer.parseInt(multi.getParameter("idx"));
			title=multi.getParameter("title");
			content=multi.getParameter("content");
			
			board.setIdx(idx);
			board.setTitle(title);
			board.setContent(content);
		
			return dao.update(board);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public int delete(HttpServletRequest req, HttpServletResponse resp) {
		int idx=Integer.parseInt(req.getParameter("idx"));
		return dao.delete(idx);
	}

	@Override
	public BoardVO replyInfo(HttpServletRequest req, HttpServletResponse resp) {
		BoardVO board=dao.findOne(Integer.parseInt(req.getParameter("idx")));
		BoardVO sendboard=new BoardVO();
		sendboard.setIdx(board.getIdx());
		sendboard.setTitle(board.getTitle());
		sendboard.setGroupid(board.getGroupid());
		sendboard.setDepth(board.getDepth());
		
		return sendboard;
	}

	@Override
	public int replyInsert(HttpServletRequest req, HttpServletResponse resp) {
		String title="";
		String content="";
		String writeName="";
		String filename="";
		String groupid="";
		String depth="";
		String savePath="C:\\project\\work\\jwork\\mvc2_responsive\\src\\main\\webapp\\file\\uploadFold";
		int fileSize=10*1024*1024;
		MultipartRequest multi=null;
		try {
			multi = new MultipartRequest(req,savePath,fileSize,"utf-8",new DefaultFileRenamePolicy());
			
			title=multi.getParameter("title");
			content=multi.getParameter("content");
			writeName=multi.getParameter("writerName");
			groupid=multi.getParameter("groupid");
			depth=multi.getParameter("depth");
			
			//file관련 처리
			Enumeration files=multi.getFileNames();
			String file=(String)files.nextElement();
			filename=multi.getFilesystemName(file);
						
			BoardVO board=new BoardVO();
			board.setTitle(title);
			board.setContent(content);
			board.setWriteId((String)req.getSession().getAttribute("id"));
			board.setWriteName((String)req.getSession().getAttribute("id"));
			board.setGroupid(Integer.parseInt(groupid));
			board.setDepth(Integer.parseInt(depth));
			board.setFilename(filename);
			return dao.replyInsert(board);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
		
	}

}
