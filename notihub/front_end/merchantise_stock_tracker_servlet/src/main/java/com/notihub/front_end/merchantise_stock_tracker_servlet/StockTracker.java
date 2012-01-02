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

package com.notihub.front_end.merchantise_stock_tracker_servlet;

import java.io.IOException;
import java.util.Hashtable;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.osgicdi.OSGiService;

import com.notihub.app_services.common.MerchantiseStockTrackerService;
import com.notihub.app_services.common.NotihubMessages.MessageType;
import com.notihub.app_services.common.ProcessStatus;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class StockTracker
 */
@WebServlet("/StockTracker")
public class StockTracker extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(StockTracker.class);
	
	@Inject @OSGiService(dynamic=true)
	private MerchantiseStockTrackerService mst;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockTracker() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.debug("Incoming REST request");
		
		MessageType mt=getMessageType(request);
		if(mt==MessageType.MT_MESSAGE_UNKNOWN){
			return;
		}
		
		ProcessStatus ps=new ProcessStatus();
		String respMsg=null;
		
		switch (mt) {
		case MT_MESSAGE_UNKNOWN:
			return;
		case MT_MERCHANTISE_SEARCH_REQ:{
			String queryStr=request.getParameter("query");
			Hashtable<String, String> incomingInfo=new Hashtable<String, String>();
			incomingInfo.put("incomingMsg", queryStr);
			respMsg=mst.processMessage(incomingInfo, MessageType.MT_MERCHANTISE_SEARCH_REQ, ps);
			break;
		}
		case MT_IN_STOCK_CHECK_REQ:{
		    String brand=request.getParameter("sb");
		    String sku=request.getParameter("sku");
		    Hashtable<String, String> incomingInfo=new Hashtable<String, String>();
		    incomingInfo.put("sb", brand);
		    incomingInfo.put("sku", sku);
		    respMsg=mst.processMessage(incomingInfo, MessageType.MT_IN_STOCK_CHECK_REQ, ps);
		}
		default:
			break;
		}
		
		response.setContentType("text/plain");
		response.getWriter().write(respMsg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	public MessageType getMessageType(HttpServletRequest request){
		String msgTypeStr=request.getParameter("msgType");
		if(msgTypeStr==null){
			logger.debug("Incoming REST request doesn't have \"msgType\" parameter. Fail to accept");
			return MessageType.MT_MESSAGE_UNKNOWN;
		}
		
		MessageType mt;
		if(msgTypeStr.equalsIgnoreCase("search")==true){
			mt=MessageType.MT_MERCHANTISE_SEARCH_REQ;
		}else if(msgTypeStr.equalsIgnoreCase("locate")==true){
		    mt=MessageType.MT_IN_STOCK_CHECK_REQ;
		}
		else{
			logger.debug("Incoming REST request msgType is unknown. Fail to accept");
			mt=MessageType.MT_MESSAGE_UNKNOWN;
		}
		
		return mt;
	}

}
