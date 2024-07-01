package ui;

import com.google.gson.Gson;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebsocketClient extends Endpoint {

    public Session session;

    public WebsocketClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

//        this.session.addMessageHandler(new MessageHandler.Whole<String>(){
//            public void onMessage(String message){
//                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//                switch (serverMessage.getServerMessageType()){
//                    case ERROR -> {
//                        error(message);
//                    }
//                    case NOTIFICATION -> {
//                        notification(message);
//                    }
//                    case LOAD_GAME -> {
//                        printGame(message);
//                    }
//                }
//            }
//        });

    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }


}
