import com.google.gson.Gson;
import com.loginid.cryptodid.WebsocketClientEndpoint;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/sinVerify")
public class SinVerificationEndpoint {

    private static Gson gson = new Gson();
    public static CountDownLatch latch;
    public static String responseToSend;
    public static int honestyProof;

    @OnOpen
    public void onOpen(Session session) throws IOException, InterruptedException {
        session.setMaxIdleTimeout(1000 * 60 * 60 * 60);
        try {
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://" + VerificationEndpoint.cppServerUrl));
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {

                    System.out.println(message);
                    honestyProof = Integer.parseInt(message);
                    System.out.println(honestyProof);
                }
            });
            clientEndPoint.sendMessage("acl_java");
            //Thread.sleep(2000);
            //clientEndPoint.sendMessage(gson.toJson(new int[]{123456789, 123123123, 987654321, 258963147, 159357456}));

        }

        catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
        latch = new CountDownLatch(1);
        latch.await();
        session.getBasicRemote().sendText(responseToSend);
    }

}