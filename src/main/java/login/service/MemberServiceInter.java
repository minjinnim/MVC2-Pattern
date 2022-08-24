package login.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MemberServiceInter {
	public boolean login(HttpServletRequest req, HttpServletResponse resp);
	public void logout(HttpServletRequest req, HttpServletResponse resp);
	public void addlogin(HttpServletRequest req, HttpServletResponse resp);
}
