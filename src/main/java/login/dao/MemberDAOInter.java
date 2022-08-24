package login.dao;

import login.vo.MemberVO;

public interface MemberDAOInter {
	public boolean login(MemberVO member);
	public MemberVO findOne(String id);
}
