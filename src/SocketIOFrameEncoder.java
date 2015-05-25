import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SocketIOFrameEncoder
        implements Encoder.Text<SocketIOFrame>{


    @Override
    public String encode(SocketIOFrame socketIOFrame) throws EncodeException {
        return (socketIOFrame.encode());
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

