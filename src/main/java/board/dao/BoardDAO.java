package board.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import board.vo.BoardVO;
import login.vo.MemberVO;

public class BoardDAO implements BoardDAOInter {
	Connection conn;
	PreparedStatement pstat;

	public BoardDAO(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "1111");
		}catch(Exception e) {
			
		}
	}
	
 	public int insert(BoardVO board){
		try {
		String sql="insert into board (idx,title,content,readcount,"
				+ "groupid,depth,groupSeq,"
				+ "writeId,writeName,writeDay,"
				+ "filename,isdel,kind)"
				+ "values(board_idx_seq.nextval,?,?,0,"
				+ "0,0,1,"
				+ "?,?,sysdate,"
				+ "?,0,'일반게시판')";

		pstat=conn.prepareStatement(sql);
		
		pstat.setString(1, board.getTitle());
		pstat.setString(2, board.getContent());
		pstat.setString(3, board.getWriteId());
		pstat.setString(4, board.getWriteName());
		pstat.setString(5, board.getFilename());
		
		int result=pstat.executeUpdate();
		conn.commit();
		return result;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}	
	}
 	/*
 	@Test
 	public void insertTest() {
 		BoardVO vo = new BoardVO();
 		vo.setTitle("게시판테스트");
 		vo.setContent("게시판테스트내용");
 		vo.setWriteId("admin");
 		vo.setWriteName("kmj");
 		vo.setFilename("");
 		insert(vo);
 		
 	}
 	*/

 	
 	public int replyInsert(BoardVO board){
		try {
		String sql="insert into board (idx,title,content,readcount,"
				+ "groupid,depth,groupSeq,"
				+ "writeId,writeName,writeDay,"
				+ "filename,isdel,kind)"
				+ "values(board_idx_seq.nextval,?,?,0,"
				+ "?,?,1,"
				+ "?,?,sysdate,"
				+ "?,0,'일반게시판')";

		pstat=conn.prepareStatement(sql);
		
		pstat.setString(1, board.getTitle());
		pstat.setString(2, board.getContent());
		pstat.setInt(3, board.getGroupid());
		pstat.setInt(4, board.getDepth());
		pstat.setString(5, board.getWriteId());
		pstat.setString(6, board.getWriteName());
		
		//file 을 받을수도 안받을수도있는 상황을 처리해야됨
		pstat.setString(7, board.getFilename());
		
		int result=pstat.executeUpdate();
		
		return result;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}	
	}
 	/*
 	@Test
 	public void replyInsertTest() {
 		BoardVO vo = new BoardVO();
 		vo.setTitle("게시판테스트댓글");
 		vo.setContent("게시판테스트댓글내용");
 		vo.setGroupid(35);
 		vo.setDepth(1);
 		vo.setWriteId("admin");
 		vo.setWriteName("kmj");
 		vo.setFilename("");
 		replyInsert(vo);
 	}
 	*/
 	
 	
	public int totalCount() {
		int totalCount=0;
		
		String sql="select count(*) from board";
		
		try{
			pstat=conn.prepareStatement(sql);
			ResultSet rs=pstat.executeQuery();
				if(rs.next()){
					//필드명을 이용하는 방법과 필드 순서를 이용하는 방법이 있음
					totalCount=rs.getInt("count(*)");
					//System.out.println("전체게시물수:"+totalCount);
				}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return totalCount;
	}
	/*
	@Test
	public void totalCountTest() {
		System.out.println(totalCount());
	}
	*/
	
	public List pageList(int startRow,int endRow,int totalPage,int currentPage,int totalCount) {
		///////////글의 시작범위와 끝범위의 데이터를 가지오는 작업//////////
		String sql="select rownum,b.* from ";
		sql+="(select rownum rn,a.* from ";
		sql+="(select * from board start with groupid=0 ";
		sql+="connect by prior idx=groupid ";
		sql+="order siblings by idx desc)a ";
		sql+="where rownum <=? ";
		sql+="order by rownum desc) b ";
		sql+="where rownum between 1 and ? ";
		sql+="order by b.rn asc";
		
		List<BoardVO> list=new ArrayList();
		
		try {
			pstat=conn.prepareStatement(sql);
			//pstat.setInt(1, endRow);
			pstat.setInt(1,currentPage*10); //왜하는건지 못들음
			
			//마지막 페이지가 10개의 게시물이 아닌 경우 나머지 값을 활용하여 범위확정 
			if(totalPage==currentPage){
				pstat.setInt(2,totalCount%10);
			}else{
				pstat.setInt(2,10);
			}
			
			ResultSet rs=pstat.executeQuery();
			
			while(rs.next()){
				BoardVO board=new BoardVO();
				board.setIdx(rs.getInt("idx"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriteName(rs.getString("writeName"));
				board.setWriteDay(rs.getDate("writeDay"));
				board.setReadcount(rs.getInt("readcount"));
				board.setDepth(rs.getInt("depth"));
				board.setFilename(rs.getString("filename"));
				board.setIsdel(rs.getInt("isdel"));
				list.add(board);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/*
	@Test
	public void pageListTest() {
		//int startRow,int endRow,int totalPage,int currentPage,int totalCount
		List<BoardVO> list=pageList(31,40,totalCount()/10,1,totalCount());
		for(BoardVO board:list) {
			System.out.println(board);
		}
	}
	*/
	
	public BoardVO findOne(int _idx){
		try {
			
			String sql="select * from board where idx=?";
			pstat=conn.prepareStatement(sql);
			pstat.setInt(1, _idx);
			ResultSet rs=pstat.executeQuery();
			if(rs.next()) {
				BoardVO board=new BoardVO();
				board.setIdx(rs.getInt("idx"));
				board.setTitle(rs.getString("title"));
				board.setContent(rs.getString("content"));
				board.setWriteId(rs.getString("writeId"));
				board.setWriteName(rs.getString("writeName"));
				board.setWriteDay(rs.getDate("writeDay"));
				board.setReadcount(rs.getInt("readcount"));
				board.setFilename(rs.getString("filename"));
				board.setDepth(rs.getInt("depth"));
				return board;
			}
			return null;
			
		}catch(Exception e) {
			return null;
		}
	}
	/*
	@Test
	public void findOneTest() {
		System.out.println(findOne(1));
	}
	*/
	public void readcountUp(int idx) {
		try {
			String sql="update board set readcount=readcount+1 where idx=?";
			pstat=conn.prepareStatement(sql);
			pstat.setInt(1, idx);
			int result=pstat.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int update(BoardVO board){
		try {
		
			String sql="update board set title=?, content=? where idx=?";
			pstat=conn.prepareStatement(sql);
			pstat.setString(1, board.getTitle());
			pstat.setString(2, board.getContent());
			pstat.setInt(3, board.getIdx());

			int result=pstat.executeUpdate();
			
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	//isdel 필드의 값을 1로 바꿔주는 작업 
	public int delete(int idx){
		try {
			String sql="update board set isdel=1 where idx=?";
			pstat=conn.prepareStatement(sql);
			pstat.setInt(1, idx);
			
			int result=pstat.executeUpdate();
			
			return result;

		}catch(Exception e) {
			return 0;
		}
	}
	
/*
 * 데이터를 완전히 삭제시키는 방법
	public int delete(int idx){
		try {
			String sql="delete from board where idx=?";
			pstat=conn.prepareStatement(sql);
			pstat.setInt(1, idx);
			
			int result=pstat.executeUpdate();
			
			return result;

		}catch(Exception e) {
			return 0;
		}
	}
*/
	
	/*
	public boolean exist(int idx){
		try {
		String sql="select * from phonebook where idx=?";
		pstat=conn.prepareStatement(sql);
		pstat.setInt(1, idx);
		ResultSet rs=pstat.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
		}catch(Exception e) {
			return false;
		}
	}
	*/
 	
	
}
