import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Proof;
import com.loginid.cryptodid.protocols.ProofParameters;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/ageProof")
public class ageProofEndpoint {

    private static Gson gson = new Gson();
    public static int attributeMinimumValue;
    @OnOpen
    public void onOpen(Session session) throws IOException{
        System.out.println("open");
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String proofParameters_json, Session session) throws  IOException {
        System.out.println("received");
        ProofParameters proofParameters = gson.fromJson(proofParameters_json, ProofParameters.class);
        Proof proof = Verifier.verify(proofParameters.claim, proofParameters.fhe, attributeMinimumValue);
        session.getBasicRemote().sendText(gson.toJson(proof));
        System.out.println("sent isgreater");
    }
}