package login.vo;

public class MemberVO {
	private int idx;
	private String id;
	private String password;

public MemberVO() {}

public MemberVO(int idx, String id, String password) {
	super();
	this.idx = idx;
	this.id = id;
	this.password = password;
}

public MemberVO(String id, String password) {
	this.id = id;
	this.password = password;
}

public int getIdx() {
	return idx;
}

public void setIdx(int idx) {
	this.idx = idx;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

@Override
public String toString() {
	return "MemberVO [idx=" + idx + ", id=" + id + ", password=" + password + "]";
}

}