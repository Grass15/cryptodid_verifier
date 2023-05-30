import com.google.gson.Gson;
import com.loginid.cryptodid.WebsocketClientEndpoint;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.ClientEndpoint;
import java.io.IOException;
import java.net.URI;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.net.URISyntaxException;

@ServerEndpoint("/verify")
public class VerificationEndpoint {

    private static Gson gson = new Gson();
    private User user;
    public static CountDownLatch latch;
    public static String[] responseToSend;
    public static final String cppServerUrl = "192.168.1.25:7070";
    public static int help = 1;
    public static int honestyProof;

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
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://" + cppServerUrl));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {

                    System.out.println(message);
                    honestyProof = Integer.parseInt(message);
                    System.out.println(honestyProof);
                }
            });

            clientEndPoint.sendMessage("requirements");
            // wait 2 seconds
            Thread.sleep(2000);
            clientEndPoint.sendMessage(requirement_json);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
        latch.await();
        user = new User(responseToSend[0], responseToSend[1], responseToSend[2], responseToSend[3], responseToSend[4], responseToSend[5], new Boolean[]{Boolean.parseBoolean(responseToSend[6]), Boolean.parseBoolean(responseToSend[7]), Boolean.parseBoolean(responseToSend[8])});
        session.getBasicRemote().sendText(gson.toJson(user));

    }
}