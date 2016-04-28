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


import com.cai.DanboCallbackInterface;
import com.cai.SslUtil;

/**
 * This class encapsulates the MQTT functionality for subscribing to a topic.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class MQTTSubscriber implements MqttCallback, Runnable {
	private Log log = LogFactory.getLog(MQTTSubscriber.class);
	DanboCallbackInterface callback;
	int state = BEGIN;
	static final int BEGIN = 0;
	static final int CONNECTED = 1;
	static final int SUBSCRIBED = 2;
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
			this.subscribe(topic, qos);
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
	 * @throws MqttException
	 */
	public MQTTSubscriber(DanboCallbackInterface callback, String topic,
			int qos, String brokerUrl, String clientId, boolean cleanSession,
			String rootCA, String privateKey, String certificate)
			throws MqttException {
		
		this.callback = callback;
		this.qos = qos;
		this.topic = topic;
		this.brokerUrl = brokerUrl;
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
			System.exit(1);
			// TODO: retry code
		}
		System.out.println("MQTTSubscriber");
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

	/**
	 * Subscribe to a topic on an MQTT server Once subscribed this method waits
	 * for the messages to arrive from the server that match the subscription.
	 * It continues listening for messages until the enter key is pressed.
	 * 
	 * @param topicName
	 *            to subscribe to (can be wild carded)
	 * @param qos
	 *            the maximum quality of service to receive messages at for this
	 *            subscription
	 * @throws MqttException
	 */
	public void subscribe(String topicName, int qos) throws Throwable {
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
				// Subscribe using a non-blocking subscribe
				Subscriber sub = new Subscriber();
				sub.doSubscribe(topicName, qos);
				break;
			case SUBSCRIBED:
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

			// if (state != FINISH && state != DISCONNECT) {
			waitForStateChange(10000);
		}
		// }
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
		System.exit(1);
		// TODO: reconnect code
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
		String stringMessage = new String(message.getPayload());
		log.debug("Message " + new String(stringMessage)
				+ " received from topic " + topic);
		callback.processMessage(stringMessage);
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
					log.debug("Connect Failed: " + exception);
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
				// If though it is a non-blocking connect an exception can be
				// thrown if validation of parms fails or other checks such
				// as already connected fail.
				e.printStackTrace();
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	/**
	 * Subscribe in a non-blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class Subscriber {
		public void doSubscribe(String topicName, int qos) {
			// Make a subscription
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the subscription is in place.
			log.debug("Subscribing to topic \"" + topicName);

			IMqttActionListener subListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log.debug("Subscribe completed");
					state = SUBSCRIBED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log.debug("Subscribe failed" + exception);
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
				client.subscribe(topicName, qos, "Subscribe sample context",
						subListener);
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
					log.debug("Disconnect failed: " + exception);
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
