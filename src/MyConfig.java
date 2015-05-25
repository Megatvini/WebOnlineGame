import javax.websocket.server.ServerEndpointConfig;

public class MyConfig extends ServerEndpointConfig.Configurator {
    private static App app = new App();

    @Override
    public <T> T getEndpointInstance(Class <T> endpointClass) throws InstantiationException {
        if (App.class.equals(endpointClass)) {
            return (T) app;
        } else {
            throw new InstantiationException();
        }
    }
}