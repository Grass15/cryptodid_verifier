import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.MG_FHE;
import com.loginid.cryptodid.protocols.Proof;
import com.loginid.cryptodid.protocols.ProofParameters;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/cppUrl")
public class VerifySINEndpoint {
    private static Gson gson = new Gson();
    public static int attributeMinimumValue;
    @OnOpen
    public void onOpen(Session session) throws IOException{
        System.out.println(VerificationEndpoint.cppServerUrl);
        session.getBasicRemote().sendText(VerificationEndpoint.cppServerUrl);
    }

    @OnMessage
    public void onMessage(String proofParameters_json, Session session) throws  IOException {

    }
}