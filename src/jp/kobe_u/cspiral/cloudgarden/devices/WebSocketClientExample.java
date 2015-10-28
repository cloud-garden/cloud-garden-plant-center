package jp.kobe_u.cspiral.cloudgarden.devices;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


@ClientEndpoint
public class WebSocketClientExample {

	Session userSession = null;

	public WebSocketClientExample(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
	}

	@OnMessage
	public void onMessage(String message) {
		String method = null;
		try {
			JSONObject jsonObj = new JSONObject(message);
			method = jsonObj.getString("method");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		switch (method) {
		case "getTemperatureAndHumidity":
			sendMessage(DevicesController.getTemperatureAndHumidity());
			break;

		case "getImage":
			sendMessage(DevicesController.getImage());
			break;

		default:
			break;
		}
	}

	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}

	public void close() {
		//todo: serverでclose処理が必要
		this.userSession = null;
	}

}
