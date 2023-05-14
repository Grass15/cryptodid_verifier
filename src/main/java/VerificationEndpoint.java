import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/verify")
public class VerificationEndpoint {

    public static final String cppServerUrl = "192.168.11.102:8080";
    private static Gson gson = new Gson();

    private User user;
    public static CountDownLatch latch;
    public static String responseToSend;

    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException {
        session.setMaxIdleTimeout(1000 * 60 * 60 * 60);
        latch = new CountDownLatch(1);
        latch.await();
        session.getBasicRemote().sendText(responseToSend);
    }

}