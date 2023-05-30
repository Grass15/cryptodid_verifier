import com.google.gson.Gson;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/sinResponse")
public class SinResponseEndpoint {
    private static Gson gson = new Gson();
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String response_json, Session session) throws InterruptedException, IOException {
        int walletHonestyProof = Integer.parseInt(response_json);
        if(walletHonestyProof == 0){
            SinVerificationEndpoint.responseToSend = String.valueOf(false);
            session.getBasicRemote().sendText(gson.toJson(new String[]{"Prover is honest", "You are NOT authorized to access the building"}));
        } else if (walletHonestyProof == VerificationEndpoint.honestyProof) {
            SinVerificationEndpoint.responseToSend = String.valueOf(true);
            session.getBasicRemote().sendText(gson.toJson(new String[]{"Prover is honest", "You are authorized to access the building"}));
        }
        else{
            SinVerificationEndpoint.responseToSend = String.valueOf(false);
            session.getBasicRemote().sendText(gson.toJson(new String[]{"Prover is dishonest", "You are NOT authorized to access the building"}));
        }
        SinVerificationEndpoint.latch.countDown();
    }
}