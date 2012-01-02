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
import java.util.Hashtable;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.NodeList;

import com.notihub.client.NotihubMessages.MessageType;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class STSearchListActivity extends ListActivity{

    private static String TAG="STSearchListActivity";
    
    private String[] getListFromResp(String resp){
        InputStream is=new ByteArrayInputStream(resp.getBytes());
        XPathReader reader = new XPathReader(is);
        String[] respList=null;
        
        String productXpath="/STMessage/merchantiseList";
        NodeList productNl=(NodeList)reader.read(productXpath, XPathConstants.NODESET);
        if(productNl!=null){
            respList=new String[productNl.getLength()];
            for(int i=0; i<productNl.getLength(); i++){
                
                StringBuffer skuXpath=new StringBuffer();
                skuXpath.append("/STMessage/merchantiseList[");
                skuXpath.append(i+1);
                skuXpath.append("]/mName");
                String sku=(String)reader.read(skuXpath.toString(), XPathConstants.STRING);
                respList[i]=sku;
            }
        }
        
        return respList;
    }
    
    private String getSelectedSku(int position, String resp) {
        InputStream is = new ByteArrayInputStream(resp.getBytes());
        XPathReader reader = new XPathReader(is);
        StringBuffer skuXpath = new StringBuffer();
        skuXpath.append("/STMessage/merchantiseList[");
        skuXpath.append(position + 1);
        skuXpath.append("]/mSku");
        String sku = (String) reader.read(
                skuXpath.toString(),
                XPathConstants.STRING);

        return sku;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st_search_list);
        
        Bundle extras = getIntent().getExtras();
        String list_item[]=null;
        final String msg=extras.getString("ResultList");
        if(extras!=null){
            list_item=getListFromResp(msg);
        }
        if(list_item==null){
            return;
        }
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.st_search_list_item, list_item));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            // When clicked, show a toast with the TextView text
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
            
            String sku=getSelectedSku(position, msg);
            RestHandler restHandler=new RestHandler();
            Hashtable<String, String> props=new Hashtable<String, String>();
            props.put("sku", sku);
            props.put("sb", "bestbuy");
            String resp=restHandler.getRestResp(MessageType.MT_IN_STOCK_CHECK_REQ, props);
            Log.v(TAG, resp);
            
            Intent i = new Intent(STSearchListActivity.this, STResultMapActivity.class);
            i.putExtra("StoreList", resp);
            startActivity(i);
          }
        });
    }
}
