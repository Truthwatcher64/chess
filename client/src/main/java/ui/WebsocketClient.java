package ui;

import javax.websocket.*;
import java.net.URI;

public class WebsocketClient extends Endpoint {

    public Session session;

    public WebsocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>(){
            public void onMessage(String message){
                System.out.println(message);
            }
        });

    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }
}
