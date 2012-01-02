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

public class Merchantise {

    private String mSku;
    private String mUID;
	private String mName;
	private String mDesp;
	private String mSalePrice;
	private ArrayList<StoreInfo> stores;
	
	public Merchantise(){
		this.mDesp=null;
		this.mName=null;
		this.mDesp=null;
		this.stores=null;
	}

	public void setmUID(String mUID) {
		this.mUID = mUID;
	}

	public String getmUID() {
		return mUID;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmName() {
		return mName;
	}

	public void setmDesp(String mDesp) {
		this.mDesp = mDesp;
	}

	public String getmDesp() {
		return mDesp;
	}

	public void setStores(ArrayList<StoreInfo> stores) {
		this.stores = stores;
	}

	public ArrayList<StoreInfo> getStores() {
		return stores;
	}

    public void setmSalePrice(String mSalePrice) {
        this.mSalePrice = mSalePrice;
    }

    public String getmSalePrice() {
        return mSalePrice;
    }

    public void setmSku(String mSku) {
        this.mSku = mSku;
    }

    public String getmSku() {
        return mSku;
    }	
	
}
