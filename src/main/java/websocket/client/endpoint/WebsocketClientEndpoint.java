package websocket.client.endpoint;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author frederick.bass
 */
@ClientEndpoint
public class WebsocketClientEndpoint
{

    private Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI)
    {
        try
        {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @OnOpen
	public void onOpen(final Session userSession) {
		this.userSession = userSession;
	}
 
	@OnClose
	public void onClose(final Session userSession, final CloseReason reason) {
		this.userSession = null;
	}
 
	@OnMessage
	public void onMessage(final String message) {
		if (messageHandler != null) {
			messageHandler.handleMessage(message);
		}
	}
 
	public void addMessageHandler(final MessageHandler msgHandler) {
		messageHandler = msgHandler;
	}
 
	public void sendMessage(final String message) {
		userSession.getAsyncRemote().sendText(message);
	}
 
	public static interface MessageHandler {
		public void handleMessage(String message);
	}
}
