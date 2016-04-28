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

package com.cai.shadow;

import java.awt.Color;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cai.DanboCallbackInterface;
import com.cai.Main;
import com.cai.PiezoBuzzer;
import com.cai.RGBLed;
import com.cai.Servo;
import com.google.gson.Gson;

/**
 * Implementation of the Callback Interface for handling Delta notifications.
 *
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class DanboShadowDeltaCallback implements DanboCallbackInterface {
	private Log log = LogFactory.getLog(DanboShadowDeltaCallback.class);
	private Gson gson = new Gson();
	String deleteMessage = "{ \"state\": null }";

	public void processMessage(String message) {
//		try {
//			log.debug("processing delta message: " + message);
//			DanboShadowDelta amazonThingShadowDelta = gson.fromJson(message,
//					DanboShadowDelta.class);
//			// action: check if there's any state to be synched
//			DanboShadowDelta.State state = amazonThingShadowDelta.getState();
//			if (state != null) {
//				if (state.getEyes() != null) {
//					if (state.getEyes().contains("blinkeyes")) {
//						log.debug("blinkeyes command received");
//
//						String rgb = state.getEyes().substring(
//								state.getEyes().lastIndexOf("_") + 1,
//								state.getEyes().length());
//						log.debug("rgb: " + rgb);
//						String eye1 = rgb.split("\\|")[0];
//						log.debug("eye1-1: " + eye1);
//						eye1 = eye1.substring(9, eye1.length() - 1);
//						log.debug("eye1-2: " + eye1);
//						String eye2 = rgb.split("\\|")[1];
//						log.debug("eye2-1: " + eye2);
//						eye2 = eye2.substring(9, eye2.length() - 1);
//						log.debug("eye2-2: " + eye2);
//
//						int repetitions = new Integer(
//								rgb.split("\\|")[2].split(":")[1]);
//						int speed = new Integer(
//								rgb.split("\\|")[3].split(":")[1]);
//
//						int r1 = new Integer(eye1.split(",")[0].trim());
//						log.debug("r1: " + r1);
//						int g1 = new Integer(eye1.split(",")[1].trim());
//						log.debug("g1: " + g1);
//						int b1 = new Integer(eye1.split(",")[2].trim());
//						log.debug("b1: " + b1);
//						int r2 = new Integer(eye2.split(",")[0].trim());
//						log.debug("r2: " + r2);
//						int g2 = new Integer(eye2.split(",")[1].trim());
//						log.debug("g2: " + g2);
//						int b2 = new Integer(eye2.split(",")[2].trim());
//						log.debug("b2: " + b2);
//
//						RGBLed rgbLed = new RGBLed("RGBLed", Main.pinLayout1,
//								Main.pinLayout2, new Color(r1, g1, b1),
//								new Color(r2, g2, b2), repetitions,
//								100 * (6 - speed));
//
//						new Thread(rgbLed).start();
//					}
//				}
//				if (state.getHead() != null) {
//					if (state.getHead().contains("movehead")) {
//						log.debug("movehead command received");
//						int angle = new Integer(state.getHead().substring(
//								state.getHead().lastIndexOf("_") + 1,
//								state.getHead().length()));
//						log.debug("angle: " + angle);
//						Servo servo = new Servo("Servo", angle);
//						new Thread(servo).start();
//					}
//				}
//				if (state.getMouth() != null) {
//					if (state.getMouth().contains("playtune")) {
//						log.debug("playtune command received");
//
//						String tune = state.getMouth().substring(
//								state.getMouth().lastIndexOf("_") + 1,
//								state.getMouth().length());
//						tune = tune.split("\\|")[1];
//
//						PiezoBuzzer piezo = new PiezoBuzzer("PiezoBuzzer", tune);
//						new Thread(piezo).start();
//					}
//				}
//
//				Main.deleteStatus("deltaDelete");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}