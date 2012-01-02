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

import java.util.Hashtable;

import com.notihub.client.NotihubMessages.MessageType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotihubActivity extends Activity {
    // Called when the activity is first created. 
    public static final String TAG = "NotihubActivity";

    private Button stSearchButton;
    private EditText stSearchEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        stSearchEditText = (EditText) findViewById(R.id.st_et_search);
        stSearchButton = (Button) findViewById(R.id.st_bn_search);
        stSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSubmitButtonClicked();
            }
        });

    }

    private void onSubmitButtonClicked() {
        String queryStr = stSearchEditText.getText().toString();
        RestHandler restHandler=new RestHandler();
        Hashtable<String, String> props=new Hashtable<String, String>();
        props.put("query", queryStr);
        String resp=restHandler.getRestResp(MessageType.MT_MERCHANTISE_SEARCH_REQ, props);
        Log.v(TAG, resp);
        
        Intent i = new Intent(this, STSearchListActivity.class);
        i.putExtra("ResultList", resp);
        startActivity(i);
    }
}