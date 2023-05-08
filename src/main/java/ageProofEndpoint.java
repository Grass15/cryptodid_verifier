import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Proof;
import com.loginid.cryptodid.protocols.ProofParameters;
import com.loginid.cryptodid.protocols.TfheVerifier;
import com.loginid.cryptodid.protocols.Verifier;
import org.apache.commons.io.FileUtils;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ServerEndpoint("/ageProof")
public class ageProofEndpoint {

    private static Gson gson = new Gson();

    List<Byte> cloudKey=new ArrayList<Byte>();
    public static int attributeMinimumValue;
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
        session.setMaxBinaryMessageBufferSize(20000000);
        session.setMaxIdleTimeout(1000 * 60 * 60 * 60);
        help = 3;
    }

//    @OnMessage
//    public void onMessage(String proofParameters_json, Session session) throws Exception {
//        ProofParameters proofParameters = gson.fromJson(proofParameters_json, ProofParameters.class);
//        Proof proof = Verifier.verify(proofParameters.claim, proofParameters.fhe, attributeMinimumValue, proofParameters.signatureBytes,proofParameters.x509Certificate);
//
//        if (proof != null) {
//            session.getBasicRemote().sendText(gson.toJson(proof));
//        } else {
//            session.getBasicRemote().sendText("Verification failed");
//        }
//
//
//    }
    int help = 3;
    byte[] answerBytes;

    @OnMessage
    public void onMessage(String proofParameters_json, Session session) throws Exception {
        int j=0;
        if(help > 0){
            byte[] cloudKeyBytes = new byte[cloudKey.size()];;
            Byte[] byteObjects = cloudKey.toArray(new Byte[0]);
            for(Byte b: byteObjects)
                cloudKeyBytes[j++] = b.byteValue();
            cloudKey.clear();
            FileUtils.writeByteArrayToFile(new File(proofParameters_json), cloudKeyBytes);
            System.out.println(proofParameters_json);
            help--;
        }
        else if(help == 0){
            TfheVerifier tfheVerifier = new TfheVerifier();
            //tfheVerifier.verifyAge();
            File file = new File("Answer.data");
            answerBytes = FileUtils.readFileToByteArray(file);
            from = 0;
            to = 20000000;
            help --;
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(Arrays.copyOfRange(answerBytes, from, answerBytes.length)));

        }
        else if(help == -1 && answerBytes.length > to){
            sendFile(session, 0);
        }
        else if(help == -1 ){
            session.getBasicRemote().sendText("Answer.data");
            new File("cloud.key").delete();
            new File("cloud.data").delete();
            new File("PK.key").delete();
            new File("Answer.data").delete();
        }

        else if(help == -2 ){
            session.getBasicRemote().sendText("Answer.data");
            new File("cloud.key").delete();
            new File("cloud.data").delete();
            new File("PK.key").delete();
            new File("Answer.data").delete();
        }


    }
    int from, to;

    public void sendFile(Session session, int indicator) throws IOException {

        System.out.println("from"+from);

        if(indicator == 0){
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(Arrays.copyOfRange(answerBytes, from, answerBytes.length)));
            from = to;
            to += 20000000;
            System.out.println("from"+from);
        }
        else{
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(Arrays.copyOfRange(answerBytes, from, answerBytes.length)));
        }
    }
    @OnMessage(maxMessageSize = 20000000)
    public void onMessage(byte[] cloudKeypiece, Session session) throws Exception {
        Byte[] byteObjects = new Byte[cloudKeypiece.length];
        int i=0;
        for(byte b: cloudKeypiece)
            byteObjects[i++] = b;
        cloudKey.addAll(Arrays.asList(byteObjects));
    }

}