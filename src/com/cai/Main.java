package com.cai;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.cai.mqtt.MQTTPublisher;
import com.cai.mqtt.MQTTSubscriber;
import com.cai.shadow.ControllerShadow;
import com.cai.shadow.DanboShadowDeltaCallback;
import com.google.gson.Gson;

public class Main {


	private static String endpoint = " "; // awsiot.endpoint
	private static String rootCA = ""; // awsiot.rootCA
	private static String privateKey = ""; // awsiot.privateKey
	private static String certificate = ""; // awsiot.certificate

	private static Gson gson = new Gson();
	private static int qos = 1;
	private static int port = 8883;
	private static boolean cleanSession = true;
	private static String clientId = "CAI_IoT";
	private static String protocol = "ssl://";
	private static String url = "";

	private static String controllerUpdateTopic = "";
	private static String updateTopic = "";
	private static String deltaTopic = "";
	private static String rejectedTopic = "";

	private static Random rand = new Random();

	public static void main(String[] args) throws Exception {

		// loads properties from danbo.properties file - make sure this file is
		// available on the pi's home directory
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");  
		Properties properties = new Properties();
		properties.load(input);
		endpoint = properties.getProperty("awsiot.endpoint");
		rootCA = properties.getProperty("awsiot.rootCA");
		privateKey = properties.getProperty("awsiot.privateKey");
		certificate = properties.getProperty("awsiot.certificate");
		url = protocol + endpoint + ":" + port;

		// AWS IoT things shadow topics
		updateTopic = "$aws/things/" + clientId + "/shadow/update";
		deltaTopic = "$aws/things/" + clientId + "/shadow/update/delta";
		rejectedTopic = "$aws/things/" + clientId + "/shadow/update/rejected";

		// AWS IoT controller things shadow topic (used to register new things)
		controllerUpdateTopic = "$aws/things/Controller/shadow/update";


		// defines an empty controller shadow POJO
		final ControllerShadow controllerShadow = new ControllerShadow();
		ControllerShadow.State controllerState = controllerShadow.new State();
		final ControllerShadow.State.Reported controllerReported = controllerState.new Reported();
		controllerReported.setThingName(clientId);
		controllerState.setReported(controllerReported);
		controllerShadow.setState(controllerState);

		try {
			String message = gson.toJson(controllerShadow);
			System.out.println(rootCA);
			MQTTPublisher controllerUpdatePublisher = new MQTTPublisher(
					controllerUpdateTopic, qos, message, url, clientId
							+ "-controllerupdate" + rand.nextInt(100000),
					cleanSession, rootCA, privateKey, certificate);
			new Thread(controllerUpdatePublisher).start();
			
			MQTTSubscriber deltaSubscriber = new MQTTSubscriber(
					new DanboShadowDeltaCallback(), deltaTopic, qos, url,
					clientId + "-delta" + rand.nextInt(100000), cleanSession,
					rootCA, privateKey, certificate);
			new Thread(deltaSubscriber).start();
			System.out.println("==============");
	
										// with an initial delay of 5 seconds
		} catch (MqttException me) {
			// Display full details of any exception that occurs
			me.printStackTrace();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
