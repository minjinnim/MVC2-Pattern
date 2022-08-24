<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<head>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
</head>
<main>
<h2 id="headertitle">${id}님이 작성하신 글</h2>

<form action="/board/update" method="post" enctype="multipart/form-data">

  <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">글번호</span>
    </div>
     <input type="text" id="idx" name="idx"  class="form-control" readonly value="${board.idx}">
  </div>
 
  <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">작성자</span>
    </div>
     <input type="text" id="writeName" name="writeName"  class="form-control" readonly value="${board.writeName}">
  </div>
 
  <div class="input-group mb-3">
     <div class="input-group-prepend">
      <span class="input-group-text">글제목</span>
    </div>
    <input type="text" id="title" name="title" class="form-control" readonly value="${board.title}">
  </div>
  <!--name:req.getParameter에서 받아올때  -->
   <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">작성한 날짜</span>
    </div>
    <input type="text" id="writeDay" name="writeDay" class="form-control" readonly value="${board.writeDay}">
  </div>
  
   <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">글내용</span>
    </div>
    <input type="text" id="content" name="content" class="form-control" readonly value="${board.content}">
  </div>
  
   <div class="input-group mb-3">
    <div class="input-group-prepend">
      <span class="input-group-text">첨부파일</span>
    </div>
    <c:if test="${not empty board.filename}">
    	<a href="/file/uploadFold/${board.filename}" download>
			<img src="/img/file.png" style="width:20px"/>
		</a>
	</c:if>
	<input type="text" id="filename" name="filename" class="form-control" readonly value="${board.filename}">
  </div>
  
<!-- 고려사항
수정,삭제: 글쓴이=id가 일치할때만 표시
수정: title,content,첨부파일(덮어쓰기가능), 수정모드(readonly 제거, submit버튼, sql=update), <h2>수정, form action을 미리 정의
삭제:
댓글: 항상 표시
-->
<c:if test="${id eq board.writeId}">
	<input type="button" class="btn btn-primary" value="수정" >
	<input type="button" class="btn btn-primary" value="삭제" >
</c:if>
<input type="button" class="btn btn-primary" value="댓글">  
<input type="button" class="btn btn-primary" value="뒤로가기">
</form>
</main>

<script>
var state="read";
$(function(){
    $("input:button").click(function(){
         if($(this).val()=="댓글"){
        	 location.href="/board/replyForm?idx=${board.idx}";
         }else if($(this).val()=="뒤로가기"){
        	 location.href="/board/list";
         }else if($(this).val()=="수정"){
        	if(state=="read"){
        		$("#headertitle").html("글 수정하기");
            	$("#title").removeAttr("readonly");
            	$("#content").removeAttr("readonly");
            	$("#filename").removeAttr("readonly");
            	state="update";
        	}else{
        		$(this).prop("type","submit");
        	}
         }else if($(this).val()=="삭제"){
        	 location.href="/board/delete?idx=${board.idx}";
         }
    });
});

</script>