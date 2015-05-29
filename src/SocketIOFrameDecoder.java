import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.List;

/**
 * Created by rezo on 5/23/15.
 */
public class SocketIOFrameDecoder implements Decoder.Text<List<SocketIOFrame>> {
    @Override
    public List<SocketIOFrame> decode(String s) throws DecodeException {
        return (SocketIOFrame.parse(s));
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
