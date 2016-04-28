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
 * Pojo for an AmazonThing Thing Shadow.
 *
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class DanboShadow {
	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public class State {
		private Reported reported;
		private Desired desired;
		private String version;
		private String clientToken;
		private Metadata metadata;

		public Reported getReported() {
			return reported;
		}

		public void setReported(Reported reported) {
			this.reported = reported;
		}

		public Desired getDesired() {
			return desired;
		}

		public void setDesired(Desired desired) {
			this.desired = desired;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getClientToken() {
			return clientToken;
		}

		public void setClientToken(String clientToken) {
			this.clientToken = clientToken;
		}

		public Metadata getMetadata() {
			return metadata;
		}

		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}

		class Metadata {
			private Reported reported;
			private Desired desired;

			public Reported getReported() {
				return reported;
			}

			public void setReported(Reported reported) {
				this.reported = reported;
			}

			public Desired getDesired() {
				return desired;
			}

			public void setDesired(Desired desired) {
				this.desired = desired;
			}

			class Reported {
				private String head;
				private String eyes;
				private String mouth;
				private String temperature;
				private String name;
				private String humidity;

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getHumidity() {
					return humidity;
				}

				public void setHumidity(String humidity) {
					this.humidity = humidity;
				}

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

				public String getTemperature() {
					return temperature;
				}

				public void setTemperature(String temperature) {
					this.temperature = temperature;
				}
			}

			class Desired {
				private String head;
				private String eyes;
				private String mouth;
				private String temperature;
				private String name;
				private String humidity;

				public String getName() {
					return name;
				}

				public void setName(String name) {
					this.name = name;
				}

				public String getHumidity() {
					return humidity;
				}

				public void setHumidity(String humidity) {
					this.humidity = humidity;
				}

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

				public String getTemperature() {
					return temperature;
				}

				public void setTemperature(String temperature) {
					this.temperature = temperature;
				}
			}
		}

		public class Reported {
			private String head;
			private String eyes;
			private String mouth;
			private String temperature;
			private String name;
			private String humidity;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getHumidity() {
				return humidity;
			}

			public void setHumidity(String humidity) {
				this.humidity = humidity;
			}

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

			public String getTemperature() {
				return temperature;
			}

			public void setTemperature(String temperature) {
				this.temperature = temperature;
			}
		}

		class Desired {
			private String head;
			private String eyes;
			private String mouth;
			private String temperature;
			private String name;
			private String humidity;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getHumidity() {
				return humidity;
			}

			public void setHumidity(String humidity) {
				this.humidity = humidity;
			}

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

			public String getTemperature() {
				return temperature;
			}

			public void setTemperature(String temperature) {
				this.temperature = temperature;
			}
		}
	}
}