<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<main>
<div class="cre_homeIcon">
	<a href="/"><i class="fa-solid fa-house"></i></a>
</div>

<div class="cre_content">
	<form name="forms" action="/member/createMemberProc.jsp" method="post">
	
		아이디:<input type="text" name="id"><br>
		비밀번호:<input type="password" name="password" id="password"><br>
		비밀번호 재입력:<input type="password" name="repassword" id="repassword"><br>
		<input type="button" onclick="check()" value="회원가입">	
	</form>
</div>
</main>