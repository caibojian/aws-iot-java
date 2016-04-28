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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class encapsulates the calls to the python script that handles the Piezo
 * Buzzer commands.
 * 
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class PiezoBuzzer implements Runnable {
	private final int OCTAVE_OFFSET = -1;

	int note_pitch[] = { 0, 262, 277, 294, 311, 330, 349, 370, 392, 415, 440,
			466, 494, 523, 554, 587, 622, 659, 698, 740, 784, 831, 880, 932,
			988, 1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760,
			1865, 1976, 2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322,
			3520, 3729, 3951, 4186, 4435, 4699, 4978 };

	private Log log = LogFactory.getLog(PiezoBuzzer.class);
	private String threadName = "";
	private String tune = "";

	public PiezoBuzzer(String threadName, String tune) {
		this.threadName = threadName;
		this.tune = tune;
	}

	public void run() {
		try {
			log.debug("starting execution of thread: " + threadName);

			log.debug("playing tune: " + tune);
			String[] returnVal = play(tune);
			String command = "sudo python /home/pi/danbo/python/buzzer.py --pitches="
					+ returnVal[0] + " --durations=" + returnVal[1];
			// call the python script that plays the tune
			log.debug("playing tune with command '" + command + "'");
			Runtime.getRuntime().exec(String.format(command));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.debug("finishing execution of thread: " + threadName);
		}
	}

	public String[] play(String songData) throws Exception {
		ArrayList<Integer> pitches = new ArrayList<Integer>();
		ArrayList<Float> durations = new ArrayList<Float>();

		int default_dur = 4;

		int default_oct = 6;

		int bpm = 63;
		int num;
		long wholenote;
		long duration;
		int note;

		int scale;

		int p = 0;
		while (songData.charAt(p) != ':') {
			p++;
		}
		p++;
		if (songData.charAt(p) == 'd') {
			p++;
			p++;

			num = 0;

			while (Character.isDigit(songData.charAt(p))) {
				num = (num * 10) + Integer.valueOf(songData.charAt(p++) - '0');
			}

			if (num > 0) {
				default_dur = num;
			}

			p++;
		}

		if (songData.charAt(p) == 'o') {
			p++;
			p++;

			num = Integer.valueOf(songData.charAt(p++) - '0');

			if (num >= 3 && num <= 7) {
				default_oct = num;
			}

			p++;
		}

		if (songData.charAt(p) == 'b') {
			p++;
			p++;

			num = 0;
			while (Character.isDigit(songData.charAt(p))) {
				num = (num * 10) + Integer.valueOf(songData.charAt(p++) - '0');
			}
			bpm = num;

			p++;
		}

		wholenote = (60 * 1000L / bpm) * 4;

		while (p < songData.length()) {
			num = 0;
			while (Character.isDigit(songData.charAt(p))) {
				num = (num * 10) + Integer.valueOf(songData.charAt(p++) - '0');
			}

			if (num != 0) {
				duration = wholenote / num;
			} else {
				duration = wholenote / default_dur;
			}
			note = 0;

			switch (songData.charAt(p)) {
			case 'c':
				note = 1;
				break;
			case 'd':
				note = 3;
				break;
			case 'e':
				note = 5;
				break;
			case 'f':
				note = 6;
				break;
			case 'g':
				note = 8;
				break;
			case 'a':
				note = 10;
				break;
			case 'b':
				note = 12;
				break;
			case 'p':
				note = 0;
			default:
				note = 0;
			}
			p++;

			if (songData.charAt(p) == '#') {
				note++;
				p++;
			}

			if (songData.charAt(p) == '.') {
				duration += duration / 2;
				p++;
			}

			if (Character.isDigit(songData.charAt(p))) {
				scale = Integer.valueOf(songData.charAt(p) - '0');
				p++;
			} else {
				scale = default_oct;
			}

			scale += OCTAVE_OFFSET;

			if (songData.charAt(p) == ',') {
				p++;
			}

			if (note != 0) {
				int n = note_pitch[(scale - 4) * 12 + note];
				pitches.add(new Integer(n));
				durations.add(new Float(duration) / new Float(1000));
			} else {
				pitches.add(new Integer(0));
				durations.add(new Float(duration) / new Float(1000));
			}
		}

		String[] returnVal = new String[2];
		returnVal[0] = pitches.toString()
				.substring(1, pitches.toString().length() - 1)
				.replaceAll(" ", "");
		returnVal[1] = durations.toString()
				.substring(1, durations.toString().length() - 1)
				.replaceAll(" ", "");
		return returnVal;
	}
}