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

package com.notihub.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.NodeList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class STResultMapActivity extends MapActivity{

    private static final String TAG="STResultMapActivity";
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private void addStoreItemizedOverlay(MapView mapView, String storeList){
        
        InputStream is=new ByteArrayInputStream(storeList.getBytes());
        XPathReader reader = new XPathReader(is);
        String sbXpath="/STMessage/storeBrand";
        String sb=(String)reader.read(sbXpath.toString(), XPathConstants.STRING);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable;
        if(sb.equalsIgnoreCase("SB_BESTBUY")){
            drawable=this.getResources().getDrawable(R.drawable.bestbuy_logo);
        }else{
            drawable=this.getResources().getDrawable(R.drawable.android);
        }
        MapItemizedOverlay itemizedoverlay = new MapItemizedOverlay(drawable, this);
        
        String productXpath="/STMessage/merchantiseList/stores";
        NodeList productNl=(NodeList)reader.read(productXpath, XPathConstants.NODESET);
        if(productNl!=null){
            for(int i=0; i<productNl.getLength(); i++){
                
                StringBuffer latXpath=new StringBuffer();
                latXpath.append("/STMessage/merchantiseList/stores[");
                latXpath.append(i+1);
                latXpath.append("]/lat");
                String lat=(String)reader.read(latXpath.toString(), XPathConstants.STRING);
                
                StringBuffer lngXpath=new StringBuffer();
                lngXpath.append("/STMessage/merchantiseList/stores[");
                lngXpath.append(i+1);
                lngXpath.append("]/lng");
                String lng=(String)reader.read(lngXpath.toString(), XPathConstants.STRING);
                Float latInt=Float.valueOf(lat).floatValue()*1000000;
                Float lngInt=Float.valueOf(lng).floatValue()*1000000;

                StringBuffer addrXpath=new StringBuffer();
                addrXpath.append("/STMessage/merchantiseList/stores[");
                addrXpath.append(i+1);
                addrXpath.append("]/address");
                String address=(String)reader.read(addrXpath.toString(), XPathConstants.STRING);
                
                StringBuffer cityXpath=new StringBuffer();
                cityXpath.append("/STMessage/merchantiseList/stores[");
                cityXpath.append(i+1);
                cityXpath.append("]/city");
                String city=(String)reader.read(cityXpath.toString(), XPathConstants.STRING);
                
                StringBuffer distXpath=new StringBuffer();
                distXpath.append("/STMessage/merchantiseList/stores[");
                distXpath.append(i+1);
                distXpath.append("]/distance");
                String distance=(String)reader.read(distXpath.toString(), XPathConstants.STRING);
                
                StringBuffer phoneXpath=new StringBuffer();
                phoneXpath.append("/STMessage/merchantiseList/stores[");
                phoneXpath.append(i+1);
                phoneXpath.append("]/phone");
                String phone=(String)reader.read(phoneXpath.toString(), XPathConstants.STRING);
                
                StringBuffer snippet=new StringBuffer();
                snippet.append(address);
                snippet.append("\n");
                snippet.append(city);
                snippet.append("  ");
                snippet.append(phone);
                snippet.append("\n");
                snippet.append(distance);
                snippet.append(" miles");
                
                GeoPoint point = new GeoPoint(latInt.intValue(), lngInt.intValue());
                OverlayItem overlayitem = new OverlayItem(point, sb, snippet.toString());
                itemizedoverlay.addOverlay(overlayitem);
            }
        }
        mapOverlays.add(itemizedoverlay);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_map);
        
        MapView mapView = (MapView) findViewById(R.id.st_result_map);
        mapView.setBuiltInZoomControls(true);
        Bundle extras = getIntent().getExtras();
        final String msg=extras.getString("StoreList");
        addStoreItemizedOverlay(mapView, msg);
    }
}
