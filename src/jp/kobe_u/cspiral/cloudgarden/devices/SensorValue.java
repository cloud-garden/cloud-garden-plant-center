package jp.kobe_u.cspiral.cloudgarden.devices;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sensors")
public class SensorValue {
	public int temperature;
	public int humidity;
	public String image;
}
