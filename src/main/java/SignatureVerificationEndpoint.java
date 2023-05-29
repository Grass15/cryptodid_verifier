import com.google.gson.Gson;
import com.loginid.cryptodid.model.SignatureVerificationParameters;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import okhttp3.*;


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

    public String isVCInRevocationTree(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public boolean verifySignature(byte[] claimBytes, byte[] signatureBytes, byte[] certificateBytes, byte[] revocationNonce, int version) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(certificateBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        X509Certificate x509Certificate = (X509Certificate) ois.readObject();
        // Create a Signature object and initialize it with the public key from the certificate
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(x509Certificate);
        verifier.update(claimBytes);
        if(verifier.verify(signatureBytes)){
            String url = "http://192.168.11.105:3000/isvcinRevocationsMerkleTree?revNonce="+ Arrays.toString(revocationNonce) +"&version="+version+"\"";
            return !Boolean.parseBoolean(this.isVCInRevocationTree(url));
        }
        else {
            return false;
        }
    }
    @OnMessage(maxMessageSize = 20000000)
    public void onMessage(String verificationParametersJson, Session session) throws Exception {
        System.out.println("JSON "+verificationParametersJson);
        SignatureVerificationParameters verificationParameters = gson.fromJson(verificationParametersJson, SignatureVerificationParameters.class);
        boolean response = verifySignature(verificationParameters.vcEncryptedData, verificationParameters.signatureBytes, verificationParameters.certificateBytes, verificationParameters.revocationNonce, verificationParameters.version);
        if(response){
            session.getBasicRemote().sendText(VerificationEndpoint.cppServerUrl);
        }else{
            session.getBasicRemote().sendText("");
        }


    }
}