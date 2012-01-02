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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.notihub.client.NotihubMessages.MessageType;

import android.util.Log;

public class RestHandler {

    final static String NOTI_SERVER_IP="192.168.164.152";
    final static String NOTI_SERVER_PORT="8080";
    final static String NOTI_ST_SERVER_PATH="/mst/StockTracker";
    public static final String TAG = "RestHandler";
    
    public String getRestResp(MessageType messageType, Hashtable<String, String> props) {

        StringBuffer r = new StringBuffer();
        try {
            // Create a URL for the desired page
            StringBuffer restStr=new StringBuffer();
            restStr.append("http://");
            restStr.append(NOTI_SERVER_IP);
            restStr.append(":");
            restStr.append(NOTI_SERVER_PORT);
            restStr.append(NOTI_ST_SERVER_PATH);
            restStr.append("?");
            restStr.append("msgType="+messageType.description);
            
            Set<String> keys=props.keySet();
            Iterator<String> itr=keys.iterator();
            String key;
            while(itr.hasNext()){
                key=itr.next();
                restStr.append("&");
                restStr.append(key);
                restStr.append("=");
                restStr.append(props.get(key));
            }
            Log.v(TAG, restStr.toString());
            URL url = new URL(restStr.toString());
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                r.append(str);
                r.append("\n");
            }
            in.close();
        } catch (MalformedURLException e) {
            Log.v(TAG, e.getMessage());
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }
        return r.toString();
    }
}
