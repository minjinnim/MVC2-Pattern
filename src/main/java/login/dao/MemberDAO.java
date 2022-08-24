package login.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import login.vo.MemberVO;

public class MemberDAO implements MemberDAOInter{
	Connection conn;
	PreparedStatement pstmt;

	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","test", "1111");
		}catch(Exception e) {
			System.out.println("database connection error");
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean login(MemberVO member) {
		String sql=null;
		ResultSet rs=null;
		
		try {
			sql="select * from member where id=? and password=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			
		}catch(Exception e) {
			System.out.println("login error");
			e.printStackTrace();
		}
		
		return false;
		
	}


	public MemberVO findOne(String id){
		String sql=null;
		ResultSet rs=null;
		MemberVO m=null;
		
		try {
			sql="select * from member where id=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				m=new MemberVO(rs.getInt("idx"),rs.getString("id"),rs.getString("password"));
				return m;
			}
		}catch(Exception e) {
			System.out.println("findOne error");
			e.printStackTrace();
		}
		return m;
	}
	
}


