import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Proof;
import com.loginid.cryptodid.protocols.ProofParameters;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.cert.X509Certificate;

@ServerEndpoint("/creditScoreProof")
public class creditScoreProofEndpoint {
    private static Gson gson = new Gson();
    public static int attributeMinimumValue;
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String proofParameters_json, Session session) throws Exception {
        ProofParameters proofParameters = gson.fromJson(proofParameters_json, ProofParameters.class);
        ByteArrayInputStream bais = new ByteArrayInputStream(proofParameters.certificateBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        X509Certificate x509Certificate = (X509Certificate) ois.readObject();
        System.out.println(x509Certificate);
        Proof proof = Verifier.verify(proofParameters.claim, proofParameters.fhe, attributeMinimumValue, proofParameters.signatureBytes, x509Certificate);
        session.getBasicRemote().sendText(gson.toJson(proof));
    }
}