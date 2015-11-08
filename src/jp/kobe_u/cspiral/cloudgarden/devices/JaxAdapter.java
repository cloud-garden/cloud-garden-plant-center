package jp.kobe_u.cspiral.cloudgarden.devices;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@Path("/api")
public class JaxAdapter {

	private static WebSocketClientExample wsc;

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/start")
	public Response start() {
		try {
			//TODO: ec2上のwebsocketサーバのアドレスに書き換える
			wsc = new WebSocketClientExample(new URI("ws://54.199.139.148/cloud_garden_server/api/ws"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println("new wsc: " + wsc);
		return Response.status(200).entity("<return>success</return>").build();
	}

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/stop")
	public Response stop() {
		//TODO: ec2上のwebsocketサーバでのsessionをリセットする必要がある
		wsc.close();
		return Response.status(200).entity("<return>success</return>").build();
	}

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/tmpsendTandH")
	public Response sendTandH() {
		System.out.println("in /tmpsendTandH");
		String message = DevicesController.getTemperatureAndHumidity();
		//wsc.sendMessage(message);
		RestClient client = new RestClient("superuser", "superuser");

		String uri = "http://54.199.139.148/cloud_garden_server/api/submitTemperatureAndH";
		client.post(uri, message, String.class, MediaType.APPLICATION_JSON_TYPE);

		return Response.status(200).entity("<return>"+ message +"</return>").build();
	}

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/tmpsendImage")
	public Response sendImage() {
		System.out.println("in /tmpsendImage");
		String message = DevicesController.getImage();
		//wsc.sendMessage(message);
		RestClient client = new RestClient("superuser", "superuser");

		String uri = "http://54.199.139.148/cloud_garden_server/api/submitImage";
		client.post(uri, message, String.class, MediaType.APPLICATION_JSON_TYPE);

		return Response.status(200).entity("<return>"+ message +"</return>").build();
	}

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/updateState")
	public Response updateState() {
		System.out.println("in /updateState");
		String jsonText = MainDriver.updateState();
		return Response.status(200).entity("<return>"+ jsonText +"</return>").build();
	}

	//デバッグ用
	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/wataring")
	public Response executeWatering() {
		System.out.println("in /updateState");
		boolean result = DevicesController.executeWatering();
		return Response.status(200).entity("<return>"+ result +"</return>").build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("startUpdating")
	public Response startUpdating() {
		System.out.println("in /startUpdating");
		MainDriver.startUpdating();
		return Response.status(200).entity("<return>success</return>").build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("stopUpdating")
	public Response stopUpdating() {
		System.out.println("in /stopUpdating");
		MainDriver.stopUpdating();
		return Response.status(200).entity("<return>success</return>").build();
	}

}
