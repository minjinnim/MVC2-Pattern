<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
	var sec=${sessiontime}
	var tid=setInterval(function(){
		document.querySelector("#sessiondiv").innerHTML="남은시간:" + (--sec);
		
		if(sec==0){
			clearTimeout(tid);
			alert('로그인 시간이 만료되었습니다');
			location.href="/login/logout";
		}
		
	},1000);
</script>

<header>
    <nav class="navbar">
        <div class="navbar_logo">
            <i class="fab fa-accusoft"></i>
            <a href="/">회사</a>
        </div>

        <ul class="navbar_menu">
            <li>회사소개</li>
            <li>사업안내</li>
            <li>예약안내</li>
            <li>갤러리</li>
            <li><a href="/board/list">게시판</a></li>
        </ul>
    
        <ul class="navbar_icon">
            <li><i class="fas fa-envelope"></i></li>
            <li><i class="fas fa-camera"></i></li>
            <li><a href="/chat/chat"><i class="fas fa-user-plus"></i></a></li>
            
            <c:if test="${empty id}">
            	<li><a href="/login/login"><i class="fa-solid fa-right-to-bracket"></i></a></li>
            </c:if>
            
            <c:if test="${not empty id}">
            	<li>
            	<a href="/login/logout">${id}[logout]</a>
            	<div id="sessiondiv">남은시간:${sessiontime}</div>
            	<a href="/login/addlogin">[로그인연장]</a>
            	</li>           
 			</c:if>
            
        </ul>

        <a href="#" class="navbar_toogleBtn">
            <i class="fas fa-bars"></i>
        </a>
    </nav>
</header>