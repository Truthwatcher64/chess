package handlers;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebsocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{
        System.out.println(message);
        session.getRemote().sendString("Copy");
    }
}
