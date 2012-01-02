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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import org.apache.log4j.Logger;

import com.notihub.app_services.common.Merchantise;
import com.notihub.app_services.common.StockTrackerMessage;
import com.notihub.app_services.common.StoreInfo;
import com.notihub.app_services.common.XPathReader;

public class BBYHandler {

    private static Logger logger = Logger.getLogger(BBYHandler.class);

    public enum SearchSortAlgo {
        SORT_BEST_MATCH, SORT_PRICE_FROM_ASC, SORT_PRICE_FROM_DSC,
        SORT_SALES_RANK_MEDIUM_TERM_ASC, SORT_SALES_RANK_MEDIUM_TERM_DSC,
        SORT_SALES_RANK_LONG_TERM_ASC, SORT_SALES_RANK_LONG_TERM_DSC,
        SORT_SALES_RANK_SHORT_TERM_ASC, SORT_SALES_RANK_SHORT_TERM_DSC
    }

    private final String BBY_API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private final String BBY_API_PREFIX = "http://api.remix.bestbuy.com";

    public String constructBBQueryString(String queryStr, SearchSortAlgo algo) {

        StringBuffer restQueryStr = new StringBuffer();

        switch (algo) {
            case SORT_BEST_MATCH: {
                restQueryStr.append(BBY_API_PREFIX);
                restQueryStr.append("/v1/products");
                restQueryStr.append("(search=" + queryStr);
                restQueryStr.append("&active=true");
                restQueryStr.append("&name=" + queryStr + "*");
                restQueryStr.append(")");
                restQueryStr.append("?apiKey=" + BBY_API_KEY);
                restQueryStr.append("&");
                restQueryStr
                        .append("sort=salePrice.dsc,salesRankMediumTerm.dsc");
                restQueryStr.append("&");
                restQueryStr.append("pageSize=10");
                restQueryStr.append("&");
                restQueryStr
                        .append("show=sku,productId,name,onSale,salePrice,regularPrice");
                break;
            }
            default:
                break;
        }

        return restQueryStr.toString();
    }

    public String constructBBCheckString(String queryStr, String zip,
            String distance) {

        StringBuffer restQueryStr = new StringBuffer();

        restQueryStr.append(BBY_API_PREFIX);
        restQueryStr.append("/v1/products");
        restQueryStr.append("(sku=" + queryStr + ")");
        restQueryStr.append("+");
        restQueryStr.append("stores(area(" + zip);
        restQueryStr.append(",");
        restQueryStr.append(distance + "))");
        restQueryStr.append("?apiKey=" + BBY_API_KEY);

        return restQueryStr.toString();
    }

    public void queryBBMerchantise(StockTrackerMessage stMsg) {

        String queryStr = constructBBQueryString(
                stMsg.getQueryString(),
                SearchSortAlgo.SORT_BEST_MATCH);
        String respMsg = null;

        RESTHandler restHandler = new RESTHandler();
        respMsg = restHandler.getRestResp(queryStr);

        processRespMsg(respMsg, stMsg);

        stMsg.setRespBulk(respMsg);
    }

    public void checkBBMerchantise(StockTrackerMessage stMsg) {

        String queryStr = constructBBCheckString(
                stMsg.getQueryString(),
                "95116",
                "10");
        String respMsg = null;

        RESTHandler restHandler = new RESTHandler();
        respMsg = restHandler.getRestResp(queryStr);

        processCheckRespMsg(respMsg, stMsg);

        stMsg.setRespBulk(respMsg);
    }

    private void processRespMsg(String respMsg, StockTrackerMessage stMsg) {

        parseDocument(respMsg, stMsg);
    }

    private void parseDocument(String respMsg, StockTrackerMessage stMsg) {

        InputStream is = new ByteArrayInputStream(respMsg.getBytes());
        XPathReader reader = new XPathReader(is);

        String productXpath = "/products/product";
        NodeList productNl = (NodeList) reader.read(
                productXpath,
                XPathConstants.NODESET);
        if (productNl != null) {
            for (int i = 0; i < productNl.getLength(); i++) {

                StringBuffer skuXpath = new StringBuffer();
                skuXpath.append("/products/product[");
                skuXpath.append(i + 1);
                skuXpath.append("]/sku");
                String sku = (String) reader.read(
                        skuXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("sku is: " + sku);

                StringBuffer productIdXpath = new StringBuffer();
                productIdXpath.append("/products/product[");
                productIdXpath.append(i + 1);
                productIdXpath.append("]/productId");
                String productId = (String) reader.read(
                        productIdXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("productId is: " + productId);

                StringBuffer salePriceXpath = new StringBuffer();
                salePriceXpath.append("/products/product[");
                salePriceXpath.append(i + 1);
                salePriceXpath.append("]/salePrice");
                String salePrice = (String) reader.read(
                        salePriceXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("salePrice is: " + salePrice);

                StringBuffer nameXpath = new StringBuffer();
                nameXpath.append("/products/product[");
                nameXpath.append(i + 1);
                nameXpath.append("]/name");
                String name = (String) reader.read(
                        nameXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("name is: " + name);

                Merchantise m = new Merchantise();
                m.setmSku(sku);
                m.setmUID(productId);
                m.setmSalePrice(salePrice);
                m.setmName(name);
                ArrayList<Merchantise> mList = stMsg.getMerchantiseList();
                if (mList != null) {
                    mList.add(m);
                } else {
                    mList = new ArrayList<Merchantise>();
                    mList.add(m);
                    stMsg.setMerchantiseList(mList);
                }
            }
        }
    }

    private void processCheckRespMsg(String respMsg, StockTrackerMessage stMsg) {

        parseSTCheckDocument(respMsg, stMsg);
    }

    private void parseSTCheckDocument(String respMsg, StockTrackerMessage stMsg) {

        InputStream is = new ByteArrayInputStream(respMsg.getBytes());
        XPathReader reader = new XPathReader(is);

        String productXpath = "/products/product";
        NodeList productNl = (NodeList) reader.read(
                productXpath,
                XPathConstants.NODESET);
        if (productNl != null) {
            for (int i = 0; i < productNl.getLength(); i++) {

                StringBuffer skuXpath = new StringBuffer();
                skuXpath.append("/products/product[");
                skuXpath.append(i + 1);
                skuXpath.append("]/sku");
                String sku = (String) reader.read(
                        skuXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("sku is: " + sku);

                StringBuffer productIdXpath = new StringBuffer();
                productIdXpath.append("/products/product[");
                productIdXpath.append(i + 1);
                productIdXpath.append("]/productId");
                String productId = (String) reader.read(
                        productIdXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("productId is: " + productId);

                StringBuffer salePriceXpath = new StringBuffer();
                salePriceXpath.append("/products/product[");
                salePriceXpath.append(i + 1);
                salePriceXpath.append("]/salePrice");
                String salePrice = (String) reader.read(
                        salePriceXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("salePrice is: " + salePrice);

                StringBuffer nameXpath = new StringBuffer();
                nameXpath.append("/products/product[");
                nameXpath.append(i + 1);
                nameXpath.append("]/name");
                String name = (String) reader.read(
                        nameXpath.toString(),
                        XPathConstants.STRING);
                logger.debug("name is: " + name);

                Merchantise m = new Merchantise();
                m.setmSku(sku);
                m.setmUID(productId);
                m.setmSalePrice(salePrice);
                m.setmName(name);
                ArrayList<Merchantise> mList = stMsg.getMerchantiseList();
                if (mList != null) {
                    mList.add(m);
                } else {
                    mList = new ArrayList<Merchantise>();
                    mList.add(m);
                    stMsg.setMerchantiseList(mList);
                }

                StringBuffer storeXpath = new StringBuffer();
                storeXpath.append("/products/product[");
                storeXpath.append(i + 1);
                storeXpath.append("]/stores/store");
                NodeList storeNl = (NodeList) reader.read(
                        storeXpath.toString(),
                        XPathConstants.NODESET);
                if (storeNl != null) {
                    for (int j = 0; j < storeNl.getLength(); j++) {

                        StringBuffer storeAddressXPath = new StringBuffer();
                        storeAddressXPath.append("/products/product[");
                        storeAddressXPath.append(i + 1);
                        storeAddressXPath.append("]/stores/store[");
                        storeAddressXPath.append(j + 1);
                        storeAddressXPath.append("]/address");
                        String storeAddress = (String) reader.read(
                                storeAddressXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("address is: " + storeAddress);

                        StringBuffer storeCityXPath = new StringBuffer();
                        storeCityXPath.append("/products/product[");
                        storeCityXPath.append(i + 1);
                        storeCityXPath.append("]/stores/store[");
                        storeCityXPath.append(j + 1);
                        storeCityXPath.append("]/city");
                        String storeCity = (String) reader.read(
                                storeCityXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("city is: " + storeCity);

                        StringBuffer storeRegionXPath = new StringBuffer();
                        storeRegionXPath.append("/products/product[");
                        storeRegionXPath.append(i + 1);
                        storeRegionXPath.append("]/stores/store[");
                        storeRegionXPath.append(j + 1);
                        storeRegionXPath.append("]/address");
                        String storeRegion = (String) reader.read(
                                storeRegionXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("region is: " + storeRegion);

                        StringBuffer storePostalXPath = new StringBuffer();
                        storePostalXPath.append("/products/product[");
                        storePostalXPath.append(i + 1);
                        storePostalXPath.append("]/stores/store[");
                        storePostalXPath.append(j + 1);
                        storePostalXPath.append("]/postalCode");
                        String storePostal = (String) reader.read(
                                storePostalXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("postal is: " + storePostal);

                        StringBuffer storeCountryXPath = new StringBuffer();
                        storeCountryXPath.append("/products/product[");
                        storeCountryXPath.append(i + 1);
                        storeCountryXPath.append("]/stores/store[");
                        storeCountryXPath.append(j + 1);
                        storeCountryXPath.append("]/country");
                        String storeCountry = (String) reader.read(
                                storeCountryXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("country is: " + storeCountry);

                        StringBuffer storeLatXPath = new StringBuffer();
                        storeLatXPath.append("/products/product[");
                        storeLatXPath.append(i + 1);
                        storeLatXPath.append("]/stores/store[");
                        storeLatXPath.append(j + 1);
                        storeLatXPath.append("]/lat");
                        String storeLat = (String) reader.read(
                                storeLatXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("lat is: " + storeLat);

                        StringBuffer storeLonXPath = new StringBuffer();
                        storeLonXPath.append("/products/product[");
                        storeLonXPath.append(i + 1);
                        storeLonXPath.append("]/stores/store[");
                        storeLonXPath.append(j + 1);
                        storeLonXPath.append("]/lng");
                        String storeLng = (String) reader.read(
                                storeLonXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("lng is: " + storeLng);

                        StringBuffer storePhoneXPath = new StringBuffer();
                        storePhoneXPath.append("/products/product[");
                        storePhoneXPath.append(i + 1);
                        storePhoneXPath.append("]/stores/store[");
                        storePhoneXPath.append(j + 1);
                        storePhoneXPath.append("]/phone");
                        String storePhone = (String) reader.read(
                                storePhoneXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("phone is: " + storePhone);

                        StringBuffer storeHourXPath = new StringBuffer();
                        storeHourXPath.append("/products/product[");
                        storeHourXPath.append(i + 1);
                        storeHourXPath.append("]/stores/store[");
                        storeHourXPath.append(j + 1);
                        storeHourXPath.append("]/hours");
                        String storeHour = (String) reader.read(
                                storeHourXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("hour is: " + storeHour);

                        StringBuffer storeDistanceXPath = new StringBuffer();
                        storeDistanceXPath.append("/products/product[");
                        storeDistanceXPath.append(i + 1);
                        storeDistanceXPath.append("]/stores/store[");
                        storeDistanceXPath.append(j + 1);
                        storeDistanceXPath.append("]/distance");
                        String storeDistance = (String) reader.read(
                                storeDistanceXPath.toString(),
                                XPathConstants.STRING);
                        logger.debug("distance is: " + storeDistance);

                        StoreInfo si = new StoreInfo();
                        si.setAddress(storeAddress);
                        si.setRegion(storeRegion);
                        si.setCity(storeCity);
                        si.setCountry(storeCountry);
                        si.setPostalCode(storePostal);
                        si.setPhone(storePhone);
                        si.setHours(storeHour);
                        si.setLat(Float.valueOf(storeLat));
                        si.setLng(Float.valueOf(storeLng));
                        si.setDistance(Float.valueOf(storeDistance));
                        ArrayList<StoreInfo> stores = m.getStores();
                        if (stores != null) {
                            stores.add(si);
                        } else {
                            stores = new ArrayList<StoreInfo>();
                            stores.add(si);
                            m.setStores(stores);
                        }
                    }
                }

            }
        }
    }
}
