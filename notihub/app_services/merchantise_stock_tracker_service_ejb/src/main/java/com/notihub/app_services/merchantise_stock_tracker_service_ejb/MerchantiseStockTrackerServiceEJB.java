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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.notihub.app_services.common.Merchantise;
import com.notihub.app_services.common.MerchantiseStockTrackerService;
import com.notihub.app_services.common.NotihubMessages.MessageEncoding;
import com.notihub.app_services.common.NotihubMessages.MessageType;
import com.notihub.app_services.common.NotihubMessages.StoreBrand;
import com.notihub.app_services.common.ProcessStatus;
import com.notihub.app_services.common.StockTrackerMessage;

import org.apache.log4j.Logger;

/**
 * Session Bean implementation class MerchantiseStockTrackerServiceEJB
 */
@Stateless
@Local({ MerchantiseStockTrackerService.class })
public class MerchantiseStockTrackerServiceEJB implements
        MerchantiseStockTrackerService {
    private static Logger logger = Logger
            .getLogger(MerchantiseStockTrackerServiceEJB.class);

    /**
     * Default constructor.
     */
    public MerchantiseStockTrackerServiceEJB() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see MerchantiseStockTrackerService#checkStockStatus(String)
     */
    public boolean checkStockStatus(String name) {
        // TODO Auto-generated method stub
        logger.debug("MerchantiseStockTrackerService is invoked");
        return false;
    }

    private StockTrackerMessage constructQueryMsg(String incomingMsg) {

        StockTrackerMessage stMsg = new StockTrackerMessage(
                MessageType.MT_MERCHANTISE_SEARCH_REQ, MessageEncoding.ME_XML);
        stMsg.setQueryString(incomingMsg);

        stMsg.setStoreBrand(StoreBrand.SB_BESTBUY);

        return stMsg;
    }

    private StockTrackerMessage constructCheckMsg(String sb, String sku) {

        StockTrackerMessage stMsg = new StockTrackerMessage(
                MessageType.MT_IN_STOCK_CHECK_REQ, MessageEncoding.ME_XML);
        stMsg.setQueryString(sku);

        if (sb.equalsIgnoreCase("bestbuy")) {
            stMsg.setStoreBrand(StoreBrand.SB_BESTBUY);
        }

        return stMsg;
    }

    private StockTrackerMessage structureMsg(
            Hashtable<String, String> incomingInfo, MessageType mType) {

        StockTrackerMessage stMsg = null;
        switch (mType) {
            case MT_MERCHANTISE_SEARCH_REQ: {
                stMsg = constructQueryMsg(incomingInfo.get("incomingMsg"));
                break;
            }
            case MT_IN_STOCK_CHECK_REQ: {
                stMsg = constructCheckMsg(
                        incomingInfo.get("sb"),
                        incomingInfo.get("sku"));
            }
            default:
                break;
        }
        return stMsg;
    }

    private void queryMerchantise(StockTrackerMessage stMsg) {

        switch (stMsg.getStoreBrand()) {
            case SB_BESTBUY: {
                BBYHandler bbyHandler = new BBYHandler();
                bbyHandler.queryBBMerchantise(stMsg);
                break;
            }
            default:
                break;
        }
    }

    private void checkMerchantise(StockTrackerMessage stMsg) {

        switch (stMsg.getStoreBrand()) {
            case SB_BESTBUY: {
                BBYHandler bbyHandler = new BBYHandler();
                bbyHandler.checkBBMerchantise(stMsg);
                break;
            }
            default:
                break;
        }
    }

    private String constructSearchRespMsg(StockTrackerMessage stMsg) {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(StockTrackerMessage.class);
            StringWriter writer = new StringWriter();
            jaxbContext.createMarshaller().marshal(stMsg, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return stMsg.toString();
        }
    }

    private String constructCheckRespMsg(StockTrackerMessage stMsg) {
        try {
            JAXBContext jaxbContext = JAXBContext
                    .newInstance(StockTrackerMessage.class);
            StringWriter writer = new StringWriter();
            jaxbContext.createMarshaller().marshal(stMsg, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            return stMsg.toString();
        }
    }

    private String filterRespMsg(String respMsg) {

        // filter out &amp;#174
        String filter1 = respMsg.replace("&amp;#174;", "");
        return filter1;
    }

    private String getRespMsg(StockTrackerMessage stMsg) {

        String respMsg = null;
        switch (stMsg.getMessageType()) {
            case MT_MERCHANTISE_SEARCH_REQ: {
                respMsg = constructSearchRespMsg(stMsg);
                respMsg = filterRespMsg(respMsg);
                break;
            }
            case MT_IN_STOCK_CHECK_REQ: {
                respMsg = constructCheckRespMsg(stMsg);
                respMsg = filterRespMsg(respMsg);
                //logger.debug(stMsg.getRespBulk());
                break;
            }
            default:
                break;
        }
        return respMsg;
    }

    public String processMessage(Hashtable<String, String> incomingInfo,
            MessageType msgType, ProcessStatus ps) {

        String respMsg = null;
        StockTrackerMessage stMsg = structureMsg(incomingInfo, msgType);

        switch (msgType) {
            case MT_MERCHANTISE_SEARCH_REQ: {
                queryMerchantise(stMsg);
                respMsg = getRespMsg(stMsg);
                break;
            }
            case MT_IN_STOCK_CHECK_REQ: {
                checkMerchantise(stMsg);
                respMsg = getRespMsg(stMsg);
                break;
            }
            default:
                break;
        }

        return respMsg;
    }

}
