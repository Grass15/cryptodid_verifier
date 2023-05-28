import com.google.gson.Gson;
import com.loginid.cryptodid.WebsocketClientEndpoint;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.SignatureVerificationParameters;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/signature")
public class SignatureVerificationEndpoint {

    private static Gson gson = new Gson();

    public static int help = 1;

    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxIdleTimeout(1000 * 60 * 60);
        session.setMaxTextMessageBufferSize(20000000);
        session.setMaxBinaryMessageBufferSize(20000000);
        //latch = new CountDownLatch(1);
    }

    public static boolean verifySignature(byte[] claimBytes, byte[] signatureBytes, byte[] certificateBytes) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(certificateBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        X509Certificate x509Certificate = (X509Certificate) ois.readObject();
        // Create a Signature object and initialize it with the public key from the certificate
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(x509Certificate);
        verifier.update(claimBytes);

        return verifier.verify(signatureBytes);
    }
    @OnMessage(maxMessageSize = 20000000)
    public void onMessage(String verificationParametersJson, Session session) throws Exception {
        System.out.println("JSON "+verificationParametersJson);
        SignatureVerificationParameters verificationParameters = gson.fromJson(verificationParametersJson, SignatureVerificationParameters.class);
        boolean response = verifySignature(verificationParameters.vcEncryptedData, verificationParameters.signatureBytes, verificationParameters.certificateBytes);
        if(response){
            session.getBasicRemote().sendText(VerificationEndpoint.cppServerUrl);
        }else{
            session.getBasicRemote().sendText("");
        }


    }
}