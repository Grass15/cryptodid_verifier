import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@ServerEndpoint("/test")
public class TestEndpoint {

    private static Gson gson = new Gson();
    private User user;
    String[] userPersonalDetails ;
    @OnOpen
    public void onOpen(Session session) throws IOException{
//        user = new User();
//        session.getBasicRemote().sendText(gson.toJson(user));
        System.out.println("open");
    }

    @OnMessage
    public void onMessage(String requirement_json, Session session) throws InterruptedException, IOException {
        System.out.println(requirement_json);
    }
}