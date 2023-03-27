import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.Verifier;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@ServerEndpoint("/verify")
public class VerificationEndpoint {

    private static Gson gson = new Gson();
    private User user;
    String[] userPersonalDetails ;

    @OnOpen
    public void onOpen(Session session) throws IOException{
//        user = new User();
//        session.getBasicRemote().sendText(gson.toJson(user));
    }
    public Thread getUserPersonalDetails(){
        return   new Thread() {
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(1111);
                    Socket socket = ss.accept();
                    InputStream inputStream = socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    userPersonalDetails = (String[]) objectInputStream.readObject();
                    socket.close();
                    ss.close();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

//    @OnMessage
//    public void onMessage(String requirement_json, Session session) throws InterruptedException, IOException {
//        Requirement requirement = gson.fromJson(requirement_json, Requirement.class);
//        Verifier balanceVerifier = new Verifier(Integer.parseInt(System.getenv("PORT")) , requirement.getBalance());
//        Verifier ageVerifier = new Verifier(8888, requirement.getAge());
//        Verifier creditScoreVerifier = new Verifier(9999, requirement.getCreditScore());
//        Thread getUserPersonalDetailsFromWallet = getUserPersonalDetails();
//        Thread balanceVerification = balanceVerifier.createVerificationThread();
//        Thread ageVerification = ageVerifier.createVerificationThread();
//        Thread creditScoreVerification = creditScoreVerifier.createVerificationThread();
//        getUserPersonalDetailsFromWallet.start();
//        balanceVerification.start();
//        ageVerification.start();
//        creditScoreVerification.start();
//        getUserPersonalDetailsFromWallet.join();
//        balanceVerification.join();
//        ageVerification.join();
//        creditScoreVerification.join();
//        user = new User(userPersonalDetails[0], userPersonalDetails[1], userPersonalDetails[2], userPersonalDetails[3], userPersonalDetails[4], userPersonalDetails[5], new Boolean[]{ageVerifier.getStatus(), balanceVerifier.getStatus(), creditScoreVerifier.getStatus()});
//        session.getBasicRemote().sendText(gson.toJson(user));
//    }
}