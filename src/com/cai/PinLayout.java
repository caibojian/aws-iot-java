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

/**
 * This class encapsulates the layout of an RGB pin layout (which GPIO the LED
 * uses for the red, green and blue pins).
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class PinLayout {
	private final int redPin;
	private final int greenPin;
	private final int bluePin;

	/**
	 * creates a pin layout for a RGB LED.
	 * 
	 * @param redPin
	 *            the GPIO pin the red LED is connected to
	 * @param greenPin
	 *            the GPIO pin the green LED is connected to
	 * @param bluePin
	 *            the GPIO pin the blue LED is connected to
	 */
	public PinLayout(int redPin, int greenPin, int bluePin) {
		this.redPin = redPin;
		this.greenPin = greenPin;
		this.bluePin = bluePin;
	}

	public int getRedPin() {
		return redPin;
	}

	public int getGreenPin() {
		return greenPin;
	}

	public int getBluePin() {
		return bluePin;
	}
}
