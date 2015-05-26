package Game.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServerConfig extends ServerEndpointConfig.Configurator {
    private static GameServer gameServer = new GameServer();

    @Override
    public <T> T getEndpointInstance(Class <T> endpointClass) throws InstantiationException {
        if (GameServer.class.equals(endpointClass)) {
            return (T) gameServer;
        } else {
            throw new InstantiationException();
        }
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("servletContext", httpSession);
    }
}