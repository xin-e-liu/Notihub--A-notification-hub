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

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "STMessage")
public class StockTrackerMessage extends NotihubMessages{

	public StockTrackerMessage(MessageType msgType, MessageEncoding msgEncode){
		super(msgType, msgEncode);
		this.setMerchantiseList(null);
		queryString=null;
		queryProperties=null;
	}
	public StockTrackerMessage(){
	    
	}
	
	@XmlElement
	private ArrayList<Merchantise> merchantiseList;
	
	@XmlElement
	private String queryString;
	
	private Hashtable<String, String> queryProperties;
	private String respBulk;

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryProperties(Hashtable<String, String> queryProperties) {
		this.queryProperties = queryProperties;
	}
	public Hashtable<String, String> getQueryProperties() {
		return queryProperties;
	}
	public void setRespBulk(String respBulk) {
	    this.respBulk = respBulk;
    }
	public String getRespBulk() {
	    return respBulk;
    }
	public void setMerchantiseList(ArrayList<Merchantise> merchantiseList) {
	    this.merchantiseList = merchantiseList;
    }
	public ArrayList<Merchantise> getMerchantiseList() {
	    return merchantiseList;
    }
	
}
