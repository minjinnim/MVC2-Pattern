<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<main>
<h2 id="headertitle">댓글</h2>

<form action="/board/replyFormProc" method="post" enctype="multipart/form-data">
 
  <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">작성자</span>
    </div>
     <input type="text" id="id" name="id"  class="form-control" readonly value="${id}">
  </div>
 
 
  <div class="input-group mb-3">
     <div class="input-group-prepend">
      <span class="input-group-text">제목</span>
    </div>
    <input type="text" id="title" name="title" class="form-control" value="re:${board.title}">
  </div>
  
   <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">글내용</span>
    </div>
    <input type="text" id="content" name="content" class="form-control">
  </div>
  
   <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">첨부파일</span>
    </div>
	<input type="file" id="filename" name="filename" class="form-control">
  </div>
  
 <input type="submit" class="btn btn-primary" value="댓글쓰기"/>
 <input type="button" class="btn btn-primary" onclick="location.href='/board/list'" value="취소"/>
 
 <c:choose>
 <c:when test="${board.groupid eq 0}"><input hidden type="text" name="groupid" value="${board.idx}"></c:when>
 </c:choose>
 
 <input hidden type="text" name="depth" value="${board.depth+1}">
 
 
</form>
</main>
</html>