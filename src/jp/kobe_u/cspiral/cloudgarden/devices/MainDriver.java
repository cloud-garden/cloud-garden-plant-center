package jp.kobe_u.cspiral.cloudgarden.devices;

import java.net.URI;
import java.net.URISyntaxException;

public class MainDriver {

	public static void main____(String args[]) {
		try {
			WebSocketClientExample wsc = new WebSocketClientExample(new URI("ws://localhost:8080/tinychat_with_jaxrs/api/ws"));

			int i = 0;
			Thread.sleep(1000);
			wsc.sendMessage("{\"uid\": \"tobao\", \"body\":\"" + ++i + "\"}");

			Thread.sleep(1000);
			wsc.sendMessage("{\"uid\": \"tobao\", \"body\":\"" + ++i + "\"}");

			Thread.sleep(1000);
			wsc.sendMessage("{\"uid\": \"tobao\", \"body\":\"" + ++i + "\"}");


			Thread.sleep(10000);
		} catch (URISyntaxException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
