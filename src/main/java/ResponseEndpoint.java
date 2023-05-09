import com.google.gson.Gson;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/finalResponse")
public class ResponseEndpoint {
    private static Gson gson = new Gson();
    @OnOpen
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(20000000);
    }

    @OnMessage
    public void onMessage(String finalResponse_json, Session session) throws InterruptedException, IOException {
        VerificationEndpoint.responseToSend = gson.fromJson(finalResponse_json, String[].class);
        VerificationEndpoint.help = -10;
        VerificationEndpoint.latch.countDown();
    }
}