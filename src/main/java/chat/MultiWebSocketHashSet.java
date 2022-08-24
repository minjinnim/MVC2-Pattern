package chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wsocket")
public class MultiWebSocketHashSet {
	static Set<Session> sessions=new HashSet<Session>();
	
	@OnMessage
	public void recvMessage(String message) {
		
		try {
			Iterator<Session> sess=sessions.iterator();
			while(sess.hasNext()) {
				Session s = sess.next();
				//00:아이디, 11:접속자수
				s.getBasicRemote().sendText("00:"+message);
				s.getBasicRemote().sendText("11:"+sessions.size());
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@OnOpen
	public void handleOpen(Session session) {
		System.out.println("접속자 수: "+sessions.size());
		sessions.add(session);
	}
	@OnClose
	public void handleClose(Session session) {
		sessions.remove(session);
	}
	@OnError
	public void handleError(Throwable error) {
		
	}
	

	
}
