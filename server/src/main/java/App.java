import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

import database.DatabaseHandler;
import websocket.WebSocketServer;

public class App {
    public static void main(String[] args) throws Exception {
        DatabaseHandler db = new DatabaseHandler();
        Server wsServ = new Server("localhost", 8080, "/api", null, WebSocketServer.class);
        try {
            wsServ.start();
        } catch (DeploymentException e) {
            e.printStackTrace();
        } finally {
            wsServ.stop();
        }
        // ServerEndpointConfig.Builder.create(WebSocketServer.class, "/api").build();
    }
}
