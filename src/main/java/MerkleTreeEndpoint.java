import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Proof;
import com.loginid.cryptodid.protocols.ProofParameters;
import com.loginid.cryptodid.protocols.SetMerkleTreeParameters;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.IOException;

@ServerEndpoint("/setMerkleTree")
public class MerkleTreeEndpoint {
    Gson gson = new Gson();
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String setMerkleTreeParameters_json, Session session) throws InterruptedException, IOException {
        SetMerkleTreeParameters setMerkleTreeParameters = gson.fromJson(setMerkleTreeParameters_json, SetMerkleTreeParameters.class);
        String[] verifierResponse = Verifier.setMerkleTree(setMerkleTreeParameters.verification, setMerkleTreeParameters.hash, setMerkleTreeParameters.proof_index);
        session.getBasicRemote().sendText(gson.toJson(verifierResponse));
    }
}