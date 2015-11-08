package jp.kobe_u.cspiral.cloudgarden.devices;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class MainDriver {
	private static boolean updating = false;

	public static void loopUpdating() {
		try {
			while(updating){
				String json = updateState();
				System.out.println(json);
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String updateState(){
		Client c = Client.create();

		//接続するアドレス
        WebResource resource = c.resource(
                "http://54.199.139.148/cloud_garden_server/api/updateState"
                );
		String jsonText = DevicesController.getState();
        String response = resource.type(MediaType.APPLICATION_JSON_TYPE).
                post(String.class, jsonText);
        System.out.println("response: " + response);

        JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(response);
			boolean needWatering = Boolean.parseBoolean((String)jsonResponse.get("watering"));
			if(needWatering){
				System.out.println("need watering");
				DevicesController.executeWatering();
			}
	        System.out.println("status: " + jsonResponse.get("status"));
	        System.out.println("watering: " + jsonResponse.get("watering"));
	        System.out.println("watering: " + needWatering);
		} catch (JSONException e) {
			System.out.println("exception!");
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println("ending");

        return jsonText;
	}

	public static void startUpdating(){
		updating = true;
		loopUpdating();
	}

	public static void stopUpdating(){
		updating = false;
		loopUpdating();
	}
}
