import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.ServerSocket;

@ServerEndpoint("/echo")
public class EchoEndpoint {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        ServerSocket ss = new ServerSocket(7777);
        System.out.println("Awaiting connexion");
        session.getBasicRemote().sendText("Awaiting connexion");
        ss.close();
    }
    @OnMessage
    public void onMessage(Session session, String message)
    {
        System.out.println(message);
        try {
            session.getBasicRemote().sendText("You sent " + message + " Count : " + WebSocketServer.getCount().get());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}