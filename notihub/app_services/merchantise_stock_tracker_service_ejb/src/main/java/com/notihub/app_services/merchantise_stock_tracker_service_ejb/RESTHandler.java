/*
Copyright 2011 codeoedoc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.notihub.app_services.merchantise_stock_tracker_service_ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

public class RESTHandler {

	private static Logger logger = Logger.getLogger(RESTHandler.class);

	public String getRestResp(String restReqStr) {
		// Replace all space into %20
	    restReqStr=restReqStr.replaceAll(" ", "%20");
		logger.debug("Req Str: "+restReqStr);
		StringBuffer respMsg = new StringBuffer();
		try {
			URL restReq = new URL(restReqStr);
			URLConnection restConn = restReq.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
			        restConn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				respMsg.append(inputLine);
			in.close();
		} catch (IOException e) {
			logger.debug("URL connection failed due to ");
			logger.debug(e.getMessage());
		}

		return respMsg.toString();
	}
}
