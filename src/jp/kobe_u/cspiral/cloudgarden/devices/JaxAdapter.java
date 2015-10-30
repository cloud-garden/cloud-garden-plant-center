package jp.kobe_u.cspiral.cloudgarden.devices;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api")
public class JaxAdapter {

	private static WebSocketClientExample wsc;

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
		return Response.status(200).entity("<return>successaa</return>").build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/stop")
	public Response stop() {
		//TODO: ec2上のwebsocketサーバでのsessionをリセットする必要がある
		wsc.close();
		return Response.status(200).entity("<return>success</return>").build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/tmpsendTandH")
	public Response sendTandH() {
		String message = DevicesController.getTemperatureAndHumidity();
		wsc.sendMessage(message);
		return Response.status(200).entity("<return>"+ message +"</return>").build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML})
	@Path("/tmpsendImage")
	public Response sendImage() {
		String message = DevicesController.getImage();
		wsc.sendMessage(message);
		return Response.status(200).entity("<return>"+ message +"</return>").build();
	}


}
