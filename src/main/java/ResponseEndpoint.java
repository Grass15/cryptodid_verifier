import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigInteger;

@ServerEndpoint("/response")
public class ResponseEndpoint {
    private static Gson gson = new Gson();
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String finalResponse_json, Session session) throws InterruptedException, IOException {
        //BigInteger[] R = gson.fromJson(finalResponse_json, BigInteger[].class);
        //VerificationEndpoint.responseToSend = Verifier.statuteOnProverResponse(R);
        session.getBasicRemote().sendText(gson.toJson(VerificationEndpoint.responseToSend ));
        VerificationEndpoint.responseToSend = finalResponse_json;
        VerificationEndpoint.latch.countDown();
    }
}