package jp.kobe_u.cspiral.cloudgarden.devices;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.github.sarxos.webcam.Webcam;
import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.ServoPhidget;


public class DevicesController {
	public static String getTemperatureAndHumidity() {
		InterfaceKitPhidget ik;
		try {
			ik = new InterfaceKitPhidget();
			ik.openAny();
			System.out.println("waiting for InterfaceKit attachment...");
			ik.waitForAttachment();

			double humidity = (ik.getSensorValue(0) * 0.1906) - 40.2;
			double temperature = (ik.getSensorValue(1) * 0.22222) - 61.11;
			ik.close();

			//{temperature: 27, humidity: 80}
			return "{\"status\": \"success\", \"temperature\": \""+ temperature +"\", \"humidity\":\"" + humidity + "\"}";
		} catch (PhidgetException e) {
			e.printStackTrace();
			return "{\"status\": \"failure\", \"message\": \"センサーからデータを取得できませんでした．\"}";
		}
	}

	public static String getImage() {
		Webcam webcam = null;
		webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam : " + webcam.getName());
			webcam.open();
			BufferedImage image = webcam.getImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			try {
				ImageIO.write(image, "png", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				byte[] b64 = Base64.encodeBase64(imageInByte);
				String imageInMime = new String(b64);
				System.out.println(imageInMime);
				return "{\"status\": \"success\", \"image\": \""+ imageInMime +"\"}";
			} catch (IOException e) {
				e.printStackTrace();
				return "{\"status\": \"failure\", \"message\": \"画像データの変換に失敗しました\"}";
			}
		} else {
			System.out.println("Failed: Webcam Not Found Error");
			return "{\"status\": \"failure\", \"message\": \"Webカメラが見つかりませんでした．\"}";
		}
	}

	public static String getState(){
		//温度湿度を取得
		InterfaceKitPhidget ik;
		double humidity;
		double temperature;
		try {
			ik = new InterfaceKitPhidget();
			ik.openAny();
			System.out.println("waiting for InterfaceKit attachment...");
			ik.waitForAttachment();

			humidity = (ik.getSensorValue(0) * 0.1906) - 40.2;
			temperature = (ik.getSensorValue(1) * 0.22222) - 61.11;
			ik.close();

			//{temperature: 27, humidity: 80}
		} catch (PhidgetException e) {
			e.printStackTrace();
			return "{\"status\": \"failure\", \"message\": \"センサーからデータを取得できませんでした．\"}";
		}

		//画像を取得
		Webcam webcam = null;
		webcam = Webcam.getDefault();
		if (webcam != null) {
			int width = 640;
			int height = 480;
			System.out.println("width: " + width + ", height: " + height);
			webcam.setViewSize(new Dimension(width, height));
			System.out.println("Webcam : " + webcam.getName());
			webcam.open();
			BufferedImage image = webcam.getImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			webcam.close();

			try {
				ImageIO.write(image, "png", baos);
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				byte[] b64 = Base64.encodeBase64(imageInByte);
				String imageInMime = new String(b64);
				System.out.println("temperature: " + temperature + "  humidity: " + humidity);
				System.out.println(imageInMime.substring(0, 100));
				return "{\"temperature\": \""+ temperature +"\", \"humidity\":\"" + humidity + "\", \"image\": \""+ imageInMime +"\"}";
			} catch (IOException e) {
				e.printStackTrace();
				return "{\"status\": \"failure\", \"message\": \"画像データの変換に失敗しました\"}";
			}
		} else {
			System.out.println("Failed: Webcam Not Found Error");
			return "{\"status\": \"failure\", \"message\": \"Webカメラが見つかりませんでした．\"}";
		}
	}


	//TODO: 水をあげるメソッド
	public static boolean executeWatering(){
		ServoPhidget servo;

		try {
			servo = new ServoPhidget();

			servo.openAny();
			System.out.println("waiting for Servo attachment...");
			servo.waitForAttachment();

			System.out.println("waterin...");
			servo.setPosition(0, 130);
			Thread.sleep(1000);
			servo.setPosition(0, 220);
			Thread.sleep(3000);
			servo.setPosition(0, 130);

			System.out.print("closing...");
			servo.close();
			servo = null;
			System.out.println(" ok");
		} catch (PhidgetException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
