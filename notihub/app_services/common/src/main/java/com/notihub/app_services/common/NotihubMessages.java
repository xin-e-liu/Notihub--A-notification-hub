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

package com.notihub.app_services.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotihubMessages {

	public enum MessageType {
		MT_MERCHANTISE_SEARCH_REQ, MT_MERCHANTISE_SEARCH_RESP, MT_IN_STOCK_CHECK_REQ, MT_IN_STOCK_CHECK_RESP, MT_MESSAGE_UNKNOWN
	}

	public enum MessageEncoding {
		ME_XML, ME_JSON
	}
	
	public enum StoreBrand {
		SB_BESTBUY
	}

	private MessageEncoding msgEncode;
	private MessageType msgType;
	private String serviceName;
	private StoreBrand storeBrand;

	public NotihubMessages(MessageType msgType, MessageEncoding msgEncode) {
		this.msgType = msgType;
		this.msgEncode = msgEncode;
	}
	
	public NotihubMessages(){
	    
	}

	public void setMessageEncode(MessageEncoding msgEncode) {
		this.msgEncode = msgEncode;
	}

	public MessageEncoding getMessageEncode() {
		return this.msgEncode;
	}

	public void setMessageType(MessageType msgType) {
		this.msgType = msgType;
	}

	public MessageType getMessageType() {
		return this.msgType;
	}
	
	public void setServiceName(String serviceName){
		this.serviceName=serviceName;
	}
	
	public String getServiceName(){
		return this.serviceName;
	}

	public String toString(MessageEncoding msgEncode) {
		switch (msgEncode) {
		case ME_XML:
			return "XML";
		case ME_JSON:
			return "JSON";
		default:
			return "";
		}
	}

	public void setStoreBrand(StoreBrand storeBrand) {
	    this.storeBrand = storeBrand;
    }

	public StoreBrand getStoreBrand() {
	    return storeBrand;
    }
}
