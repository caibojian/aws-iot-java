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
 * Pojo for the Controller Thing Shadow.
 *
 * @author Fabio Silva (silfabio@amazon.com)
 */
public class ControllerShadow {
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

		public class Metadata {
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
				private String thingName;

				public String getThingName() {
					return thingName;
				}

				public void setThingName(String thingName) {
					this.thingName = thingName;
				}
			}

			class Desired {
				private String thingName;

				public String getThingName() {
					return thingName;
				}

				public void setThingName(String thingName) {
					this.thingName = thingName;
				}
			}
		}

		public class Reported {
			private String thingName;

			public String getThingName() {
				return thingName;
			}

			public void setThingName(String thingName) {
				this.thingName = thingName;
			}
		}

		public class Desired {
			private String thingName;

			public String getThingName() {
				return thingName;
			}

			public void setThingName(String thingName) {
				this.thingName = thingName;
			}
		}
	}
}