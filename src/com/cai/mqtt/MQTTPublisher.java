/** 
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use 
 * this file except in compliance with the License. A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under the License.
 */

package com.cai.mqtt;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.cai.SslUtil;


/**
 * This class encapsulates the MQTT functionality for publishing to a topic.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class MQTTPublisher implements MqttCallback, Runnable {
	private Log log = LogFactory.getLog(MQTTPublisher.class);
	int state = BEGIN;
	static final int BEGIN = 0;
	static final int CONNECTED = 1;
	static final int PUBLISHED = 2;
	static final int DISCONNECTED = 3;
	static final int FINISH = 4;
	static final int ERROR = 5;
	static final int DISCONNECT = 6;
	private String threadName = "";
	private String topic = "";
	private int qos = 1;
	private String clientId = "";
	private String rootCA = "";
	private String privateKey = "";
	private String certificate = "";
	private String message = "";
	MqttAsyncClient client;
	String brokerUrl;
	private MqttConnectOptions conOpt;
	private boolean clean;
	Throwable ex = null;
	Object waiter = new Object();
	boolean donext = false;

	public void run() {
		try {
			log.debug("Starting Thread: " + threadName);
			this.publish(topic, qos, message.getBytes());
		} catch (Throwable e) {
			log.error(e.toString());
			e.printStackTrace();
		} finally {
			log.debug("Finishing Thread: " + threadName);
		}
	}

	/**
	 * Constructs an instance of the sample client wrapper
	 * 
	 * @param brokerUrl
	 *            the url to connect to
	 * @param clientId
	 *            the client id to connect with
	 * @param cleanSession
	 *            clear state at end of connection or not (durable or
	 *            non-durable subscriptions)
	 * @param quietMode
	 *            whether debug should be printed to standard out
	 * @throws MqttException
	 */
	public MQTTPublisher(String topic, int qos, String message,
			String brokerUrl, String clientId, boolean cleanSession,
			String rootCA, String privateKey, String certificate)
			throws MqttException {
		
		this.qos = qos;
		this.topic = topic;
		this.message = message;
		this.brokerUrl = brokerUrl;
		System.out.println(brokerUrl);
		this.clientId = clientId;
		this.threadName = clientId;
		this.clean = cleanSession;
		this.rootCA = rootCA;
		this.privateKey = privateKey;
		this.certificate = certificate;
		MemoryPersistence dataStore = new MemoryPersistence();

		try {
			// Construct the object that contains connection parameters
			// such as cleanSession and LWT
			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(clean);

			try {
				conOpt.setSocketFactory(SslUtil.getSslSocketFactory(
						this.rootCA, this.certificate, this.privateKey,
						"password"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Construct the MqttClient instance
			client = new MqttAsyncClient(this.brokerUrl, this.clientId,
					dataStore);

			// Set this wrapper as the callback handler
			client.setCallback(this);
		} catch (MqttException e) {
			e.printStackTrace();
			log.error("Unable to set up client: " + e.toString());
		}
		System.out.println("MQTTPublisher");
	}

	/**
	 * Publish / send a message to an MQTT server
	 * 
	 * @param topicName
	 *            the name of the topic to publish to
	 * @param qos
	 *            the quality of service to delivery the message at (0,1,2)
	 * @param payload
	 *            the set of bytes to send to the MQTT server
	 * @throws MqttException
	 */
	public void publish(String topicName, int qos, byte[] payload)
			throws Throwable {
		// Use a state machine to decide which step to do next. State change
		// occurs
		// when a notification is received that an MQTT action has completed
		while (state != FINISH) {
			switch (state) {
			case BEGIN:
				// Connect using a non-blocking connect
				MqttConnector con = new MqttConnector();
				con.doConnect();
				break;
			case CONNECTED:
				// Publish using a non-blocking publisher
				Publisher pub = new Publisher();
				pub.doPublish(topicName, qos, payload);
				break;
			case PUBLISHED:
				Thread.sleep(2000);
				state = DISCONNECT;
				donext = true;
				break;
			case DISCONNECT:
				Disconnector disc = new Disconnector();
				disc.doDisconnect();
				break;
			case ERROR:
				throw ex;
			case DISCONNECTED:
				state = FINISH;
				donext = true;
				break;
			}

			if (state != FINISH) {
				waitForStateChange(30000);
			}
		}
	}

	/**
	 * Wait for a maximum amount of time for a state change event to occur
	 * 
	 * @param maxTTW
	 *            maximum time to wait in milliseconds
	 * @throws MqttException
	 */
	private void waitForStateChange(int maxTTW) throws MqttException {
		synchronized (waiter) {
			if (!donext) {
				try {
					waiter.wait(maxTTW);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error("Timed out");
				}

				if (ex != null) {
					throw (MqttException) ex;
				}
			}
			donext = false;
		}
	}

	/****************************************************************/
	/* Methods to implement the MqttCallback interface */
	/****************************************************************/

	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection
		// logic at this point. This sample simply exits.
		log.debug("Connection to " + brokerUrl + " lost!" + cause);
	}

	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
	}

	/**
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message)
			throws MqttException {
		// Called when a message arrives from the server that matches any
		// subscription made by the client
		String time = new Timestamp(System.currentTimeMillis()).toString();
		log.debug("Time:\t" + time + "  Topic:\t" + topic + "  Message:\t"
				+ new String(message.getPayload()) + "  QoS:\t"
				+ message.getQos());
	}

	/**
	 * Connect in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class MqttConnector {

		public MqttConnector() {
		}

		public void doConnect() {
			// Connect to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the connect completes
			log.debug("Connecting to " + brokerUrl);

			IMqttActionListener conListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Connected");
					state = CONNECTED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Connect failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Connect using a non-blocking connect
				client.connect(conOpt, "Connect sample context", conListener);
			} catch (MqttException e) {
				e.printStackTrace();
				// If though it is a non-blocking connect an exception can be
				// thrown if validation of parms fails or other checks such
				// as already connected fail.
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	/**
	 * Publish in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class Publisher {
		public void doPublish(String topicName, int qos, byte[] payload) {
			// Send / publish a message to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the message has been delivered
			MqttMessage message = new MqttMessage(payload);
			message.setQos(qos);

			log.debug("Publishing message " + message + " to topic "
					+ topicName);

			// Setup a listener object to be notified when the publish
			// completes.
			//
			IMqttActionListener pubListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Message Published");
					state = PUBLISHED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Publish failed: " + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Publish the message
				client.publish(topicName, message, "AmazonThing Pub context",
						pubListener);
			} catch (MqttException e) {
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	/**
	 * Disconnect in a non-blocking way and then sit back and wait to be
	 * notified that the action has completed.
	 */
	public class Disconnector {
		public void doDisconnect() {
			// Disconnect the client
			log.debug("Disconnecting");

			IMqttActionListener discListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Disconnect Completed");
					state = DISCONNECTED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Disconnect failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				client.disconnect("Disconnect sample context", discListener);
			} catch (MqttException e) {
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}
}
