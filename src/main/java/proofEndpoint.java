import com.google.gson.Gson;
import com.loginid.cryptodid.protocols.TfheVerifier;
import org.apache.commons.io.FileUtils;

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
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/cppUrl")
public class proofEndpoint {

    private static Gson gson = new Gson();

    List<Byte> cloudKey=new ArrayList<Byte>();
    public static int attributeMinimumValue;
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.getBasicRemote().sendText(VerificationEndpoint.cppServerUrl);
    }

    @OnMessage
    public void onMessage(String proofParameters_json, Session session) throws Exception {

    }
}