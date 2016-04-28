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

/**
 * Pojo for Delta messages.
 *
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class DanboShadowDelta {
	private String version;
	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	class State {
		private String head;
		private String eyes;
		private String mouth;

		public String getHead() {
			return head;
		}

		public void setHead(String head) {
			this.head = head;
		}

		public String getEyes() {
			return eyes;
		}

		public void setEyes(String eyes) {
			this.eyes = eyes;
		}

		public String getMouth() {
			return mouth;
		}

		public void setMouth(String mouth) {
			this.mouth = mouth;
		}
	}
}