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

package com.cai;

import java.awt.Color;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class encapsulates the calls to the python script that handles RGB Led commands.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class RGBLed implements Runnable {
	private Log log = LogFactory.getLog(RGBLed.class);
    private PinLayout pinLayout1;
    private PinLayout pinLayout2;
    private int delay;
    private int repetitions;
    private Color color1;
    private Color color2;
    
	private String threadName = "";

	public RGBLed(String threadName, PinLayout pinLayout1, PinLayout pinLayout2, Color color1, Color color2, int repetitions, int delay) {
		this.threadName = threadName;
		this.pinLayout1 = pinLayout1;
		this.pinLayout2 = pinLayout2;
		this.delay = delay;
        this.repetitions = repetitions;
        this.color1 = color1;
        this.color2 = color2;
	}

	public void run() {
		try {
			log.debug("starting execution of thread: " + threadName);
	        // calls python code to turn the RGB Led according to the selected color
			try {
				log.debug("operating rgb leds with command 'sudo python /home/pi/danbo/python/rgbled.py " + pinLayout1.getRedPin() + " " + pinLayout1.getGreenPin() + " " + pinLayout1.getBluePin() + " " + pinLayout2.getRedPin() + " " + pinLayout2.getGreenPin() + " " + pinLayout2.getBluePin() + " " + color1.getRed() + " " + color1.getGreen() + " " + color1.getBlue() + " " + color2.getRed() + " " +  color2.getGreen() + " " +  color2.getBlue() + " " +  repetitions + " " + delay + "'");
				Runtime.getRuntime().exec(
						String.format("sudo python /home/pi/danbo/python/rgbled.py %d %d %d %d %d %d %d %d %d %d %d %d %d %d", pinLayout1.getRedPin(), pinLayout1.getGreenPin(), pinLayout1.getBluePin(), pinLayout2.getRedPin(), pinLayout2.getGreenPin(), pinLayout2.getBluePin(), color1.getRed(), color1.getGreen(), color1.getBlue(), color2.getRed(), color2.getGreen(), color2.getBlue(), repetitions, delay));
			} catch (Exception e) {
				System.err.println(String.format("Could not execute the RGB LED script"));
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.debug("finishing execution of thread: " + threadName);
		}
	}
}