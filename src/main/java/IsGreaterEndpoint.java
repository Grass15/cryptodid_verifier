import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.IsGreaterParameters;
import com.loginid.cryptodid.protocols.MG_FHE;
import com.loginid.cryptodid.protocols.Verifier;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/isGreater")
public class IsGreaterEndpoint {
    private static Gson gson = new Gson();
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String isGreaterParameters_json, Session session) throws IOException {
        IsGreaterParameters isGreaterParameters = gson.fromJson(isGreaterParameters_json, IsGreaterParameters.class);
        MG_FHE.MG_Cipher er_verify = Verifier.is_greater1(isGreaterParameters.ciphers, isGreaterParameters.base, isGreaterParameters.current, isGreaterParameters.fhe);
        session.getBasicRemote().sendText(gson.toJson(er_verify));
    }
}