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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cai.DanboCallbackInterface;
import com.google.gson.Gson;

/**
* Implementation of the Callback Interface for handling Reject notifications.
*
* @author  Fabio Silva (silfabio@amazon.com)
*/
public class DanboShadowRejectedCallback implements DanboCallbackInterface {
	private Gson gson = new Gson();
	private Log log = LogFactory.getLog(DanboShadowRejectedCallback.class);

	public void processMessage(String message) {
//		try {
//			log.debug("processing error message: " + message);
//			DanboShadowRejected amazonThingShadowRejected = gson.fromJson(message,
//					DanboShadowRejected.class);
//			if (amazonThingShadowRejected.getCode() != null) {
//					log.error("Timestamp: " + amazonThingShadowRejected.getTimestamp() + " / Code: " +  amazonThingShadowRejected.getCode() + " / Message: " + amazonThingShadowRejected.getMessage());
//			} 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}