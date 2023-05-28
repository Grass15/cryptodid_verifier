import com.google.gson.Gson;
import com.loginid.cryptodid.WebsocketClientEndpoint;
import com.loginid.cryptodid.model.Claim;
import com.loginid.cryptodid.model.SignatureVerificationParameters;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

@ServerEndpoint("/verify")
public class SignatureVerificationEndpoint {

    private static Gson gson = new Gson();
    private User user;
    public static CountDownLatch latch;
    public static String[] responseToSend;
    public static final String cppServerUrl = "192.168.11.102:8080";
    public static int help = 1;

    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxIdleTimeout(1000 * 60 * 60);
        //latch = new CountDownLatch(1);
    }

    public static boolean verifySignature(byte[] claimBytes, byte[] signatureBytes, X509Certificate certificate) throws Exception {
        // Create a Signature object and initialize it with the public key from the certificate
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(certificate);
        verifier.update(claimBytes);

        return verifier.verify(signatureBytes);
    }
    @OnMessage
    public void onMessage(String verificationParametersJson, Session session) throws InterruptedException, IOException {

        SignatureVerificationParameters verificationParameters = gson.fromJson(verificationParametersJson, SignatureVerificationParameters.class);
        boolean response = verifySignature(verificationParameters.vcEncryptedData, verificationParameters.signatureBytes, );
        System.out.println();
        session.getBasicRemote().sendText(gson.toJson(user));

    }
}