<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.connection{
		margin-top:100px;
	}
</style>
<script>
	//클라이언트 소켓 생성
	var websocket=new WebSocket("ws://192.168.0.70:8080/wsocket");

	websocket.onopen=function(message){
		console.log('open');
	}
	websocket.onclose=function(message){
		console.log('close');
	}
	websocket.onerror=function(message){
		console.log('error');
	}
	//오는 메세지
	websocket.onmessage=function(message){
		//document.querySelector("#message").innerHTML+=message+"<br>"; => 결과: message는 object 이여서 [object MessageEvent]로 뜸
		
		
		var msg=message.data.split(":");
		console.log(msg[0]);
		console.log(msg[1]);
		
		if(msg[0]=="00"){
			document.querySelector("#message").innerHTML+=msg[1]+"<br>"; //따라서 message.data를 하면 이런 오류가 수정됨	
		}else if(msg[0]=="11"){
			document.querySelector("#connection").innerHTML="접속자수:"+msg[1];
		}
		
	}
	
	function sendmessage(){
		//alert("${id}");
		websocket.send("["+"${id}"+"]"+document.querySelector("#smsg").value);
	}
	
</script>
</head>
<body>
	<div id="connection"></div>
	<div class="input-group mb-3">
	 <input type="text" class="form-control" id="smsg" name="smsg" placeholder="메세지를 입력하세요.">
	 <div class="input-group-append">	
		<button type="button" class="btn btn-primary" onclick="sendmessage()">전송</button>
	 </div>
	</div>
	
	<div id="message" style="width:400px; height:400px; border:5px solid navy">
	
	</div>
</body>
</html>