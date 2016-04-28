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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class encapsulates the calls to the python script that handles simple
 * LED commands.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class Led {
	private int pinNumber;
	private Log log = LogFactory.getLog(Led.class);

	Led(int pinNumber) {
		this.pinNumber = pinNumber;
	}

	public void on() {
		// call the python script that turns the led on
		try {
			log.debug("operating led with command '"
					+ "sudo python /home/pi/danboard-thing/python/led.py "
					+ pinNumber + " on" + "'");
			Runtime.getRuntime()
					.exec(String
							.format("sudo python /home/pi/danboard-thing/python/led.py %d %s",
									pinNumber, "on"));
		} catch (Exception e) {
			System.err.println(String.format(
					"Could not execute turn Led on - pin number: ", pinNumber));
			e.printStackTrace();
		}
	}

	public void off() {
		// call the python script that turns the led off
		try {
			log.debug("operating led with command '"
					+ "sudo python /home/pi/danboard-thing/python/led.py "
					+ pinNumber + " off" + "'");
			Runtime.getRuntime()
					.exec(String
							.format("sudo python /home/pi/danboard-thing/python/led.py %d %s",
									pinNumber, "off"));
		} catch (Exception e) {
			System.err
					.println(String.format(
							"Could not execute turn Led off - pin number: ",
							pinNumber));
			e.printStackTrace();
		}
	}
}
