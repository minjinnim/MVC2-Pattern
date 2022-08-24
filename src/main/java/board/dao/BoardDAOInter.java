package board.dao;

import java.util.List;

import board.vo.BoardVO;

public interface BoardDAOInter {
	public int insert(BoardVO board);
	public int totalCount();
	public List pageList(int startRow,int endRow,int totalPage,int currentPage,int totalCount);
	public BoardVO findOne(int idx);
	public void readcountUp(int idx);
	public int update(BoardVO board);
	public int delete(int idx);
	public int replyInsert(BoardVO board);

}
