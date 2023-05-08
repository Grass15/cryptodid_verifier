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

    private static Gson gson = new Gson();
    private User user;
    public static CountDownLatch latch;
    public static String[] responseToSend;

    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxIdleTimeout(1000 * 60 * 60);
        latch = new CountDownLatch(1);
    }
    @OnMessage
    public void onMessage(String requirement_json, Session session) throws InterruptedException, IOException {
        Requirement requirement = gson.fromJson(requirement_json, Requirement.class);
        ageProofEndpoint.attributeMinimumValue = requirement.getAge();
        balanceProofEndpoint.attributeMinimumValue = requirement.getBalance();
        creditScoreProofEndpoint.attributeMinimumValue = requirement.getCreditScore();
        latch.await();// Pause
        user = new User(responseToSend[0], responseToSend[1], responseToSend[2], responseToSend[3], responseToSend[4], responseToSend[5], new Boolean[]{Boolean.parseBoolean(responseToSend[6]), Boolean.parseBoolean(responseToSend[7]), Boolean.parseBoolean(responseToSend[8])});
        session.getBasicRemote().sendText(gson.toJson(user));
    }
}