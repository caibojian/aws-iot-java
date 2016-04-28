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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements the execution of a servo command. It actually only
 * encapsulates calls to the servoblaster driver.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class Servo implements Runnable {
	public static int currentAngle = 0;
	private Log log = LogFactory.getLog(Servo.class);
	private String threadName = "";
	private int angle;

	public Servo(String threadName, int angle) {
		this.threadName = threadName;
		this.angle = angle;
	}

	public void run() {
		try {
			log.debug("starting execution of thread: " + threadName);
			// calls the servoblaster command to move the servo according to the
			// selected angle
			try {
				String servoBlasterDevice = "/dev/servoblaster";
				File servoBlasterDev = new File(servoBlasterDevice);
				if (!servoBlasterDev.exists()) {
					throw new FileNotFoundException(
							"Servoblaster not found at "
									+ servoBlasterDevice
									+ ". Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
				}
				FileWriter writer = new FileWriter(servoBlasterDev);
				StringBuilder b = new StringBuilder();
				b.append("0").append('=').append(Integer.toString(angle + 150))
						.append('\n');
				try {
					writer.write(b.toString());
					writer.flush();
				} catch (IOException e) {
					try {
						writer.close();
					} catch (IOException ignore) {
					}
				}
				try {
					writer.write(b.toString());
					writer.flush();
				} catch (IOException e) {
					throw new RuntimeException(
							"Failed to write to /dev/servoblaster device", e);
				}
			} catch (Exception e) {
				System.err.println(String
						.format("Could not execute the servoblaster command"));
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.debug("finishing execution of thread: " + threadName);
		}
	}
}