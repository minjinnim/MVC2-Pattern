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
		
		//전체게시물 수
		int totalCount=0;
		//페이지당 글의 수
		int countPerPage=10;
		//전체페이지수
		int totalPage=0;
		//시작페이지
		int startPage=0;
		//끝페이지
		int endPage=0;
		//글의 시작번호
		int startRow=0;
		//글의 끝번호
		int endRow=0;
		//현재 페이지
		int currentPage=1;
		
		List<BoardVO> list = null;
		
		PageList plist=new PageList();
		
		//전체게시물 수 check
		totalCount=dao.totalCount();
		
		//전체페이지수
		totalPage=(totalCount/countPerPage)+1;
		//전체게시물수가 나머지 0이될 경우 페이지는 1페이지가 작아짐
		if((totalCount%countPerPage)==0) 
		totalPage=(totalCount/countPerPage);
		
		//전달되어지는 현재페이지를 확인
		String _currentPage=req.getParameter("currentPage");
		if(_currentPage==null) {
			currentPage=1;
		}else if(!_currentPage.equals("")) {
			currentPage=Integer.parseInt(_currentPage);
		}
		
		//현재페이지에 대한 시작번호와 끝번호
		startRow=(currentPage-1)*countPerPage+1;
		endRow=startRow+countPerPage-1;
				
		//위에서 확인한 정보를 이용해 페이지의 dao로부터 정보를 가져옴
		list=dao.pageList(startRow, endRow, totalPage, currentPage, totalCount);
				
		//표시할 시작페이지
		if(currentPage<=5){
			startPage=1;	
		}else{
			if(currentPage%5==0){
				startPage=(currentPage/5)*5;	
			}else{
				startPage=(currentPage/5)*5+1; //--처리 10페이지일 때 문제 발생
			}
		}
		
		//표시할 끝페이지
		endPage=startPage+4;
		
		//전체페이지가 표현하려는 페이지보다 클 경우 전체페이지수까지 표시 
		if(endPage>totalPage) endPage=totalPage;
				
		//findAll.jsp에 view를 하기위해 필요한 데이터만 담아둠(Model)
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
			//null이 뜸, request로 못받는다는 말
			//System.out.println("title:"+req.getParameter("title"));
			//System.out.println("content:"+req.getParameter("contnet"));
			
			//filesize에서 exception오류가 뜨면 처리해주는 오류코드를 작성해줄줄 알아한다..
			//폼에서 전달되는 데이터+파일데이터 multi안에 존재
			//req를 통해서 getinputstream을 할수있는것
			multi = new MultipartRequest(req,savePath,fileSize,"utf-8",new DefaultFileRenamePolicy());
			//파일의 이름들을 다 가져옴(여러개의 이름을 저장)
			Enumeration files=multi.getFileNames();
			
			//넘어오는 파일의 종류
			//파일이업로드됨
			String file=(String)files.nextElement();
			
			//넘어오는 파일의 이름을 가져옴, 사용함
			//서버에 저장되는 파일이름, 만약 같은 이름 존재하면 뒤에 숫자가 증가하는 형태
			//실제 저장되는 파일명
			String title=multi.getParameter("title");
			String content=multi.getParameter("content");
			String filename = multi.getFilesystemName(file);
			/*
			System.out.println("title:"+title);
			System.out.println("content:"+content);
			System.out.println("filename:"+filename);
			*/

			//클라이언트에서 넘어오는 파일명, 사용하지않음
		    //String orignfilename = multi.getOriginalFileName(file);
			//System.out.println("orign:"+orignfilename);
			
			//얘는됨
			//System.out.println("title:"+multi.getParameter("title"));
			//System.out.println("content:"+multi.getParameter("content"));
			
			//얘도안됨
			//System.out.println("filename:"+multi.getParameter("filename"));
			
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
			
			//일반 request.getParameter로 못받아 multi가 필요함
			multi = new MultipartRequest(req,savePath,fileSize,"utf-8",new DefaultFileRenamePolicy());
			
			//폼에서 전달받은 데이터를 먼저 입력받도록 함
			title=multi.getParameter("title");
			content=multi.getParameter("content");
			writeName=multi.getParameter("writerName");
			groupid=multi.getParameter("groupid");
			depth=multi.getParameter("depth");
			
			//file관련 처리
			Enumeration files=multi.getFileNames();
			String file=(String)files.nextElement();
			filename=multi.getFilesystemName(file);
						
			//전달받은 데이터를 다시 넘겨줌
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
