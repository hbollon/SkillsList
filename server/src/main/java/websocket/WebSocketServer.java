package websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
    value = "/api",
    configurator = CustomWebSocketConfig.class
)
public class WebSocketServer {
    @OnOpen
    public void onOpen(Session s, EndpointConfig config){
        System.out.println("Websocket opened!");
    }

    @OnMessage
    public void onMessage(String msg, Session s) throws IOException {
        System.out.println("Message: " + msg);
        s.getBasicRemote().sendText(msg);
    }

    @OnClose
    public void onClose(CloseReason reason, Session s) {
        System.out.println("Websocket closed!");
    }

    @OnError
    public void onError(Session s, Throwable err) {
        err.printStackTrace();
    }

}
