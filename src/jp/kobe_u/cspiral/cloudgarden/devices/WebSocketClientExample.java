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
		System.out.println("in @OnOpen");
		this.userSession = userSession;
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("in @OnMessage");
		System.out.println("message is " + message);
		String method = null;
		try {
			JSONObject jsonObj = new JSONObject(message);
			method = jsonObj.getString("method");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if(method == null){
			System.out.println("method is null.");
			return;
		}

		switch (method) {
		case "getTemperatureAndHumidity":
			System.out.println("get request of temperature and humidty");
			System.out.println(message);
			sendMessage(DevicesController.getTemperatureAndHumidity());
			break;

		case "getImage":
			System.out.println("get request of image");
			System.out.println(message);
			sendMessage(DevicesController.getImage());
			break;

		default:
			System.out.println(message);
			break;
		}
	}

	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}

	public void close() {
		//todo: serverでclose処理が必要
		sendMessage("{\"request\": \"close\"}");
		this.userSession = null;
	}

}
