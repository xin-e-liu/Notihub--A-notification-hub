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

public class StoreInfo {
	
	public enum Unit{
		US_DOLLOR,
		RMB
	}
	public enum Status{
		IN_STOCK,
		OUT_STOCK
	}

	private String storeDesp;
	private Float mPrice;
	private Unit mUnit;
	private Status stockStatus;
	private ArrayList<String> merchantise;
	public StoreInfo(){
		this.storeDesp=null;
		this.mPrice=null;
		this.mUnit=null;
		this.stockStatus=null;
		this.merchantise=null;
	}
    
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private Float lat;
    private Float lng;
    private String phone;
    private String hours;
	private Float distance;
	
	public void setStoreDesp(String storeDesp) {
		this.storeDesp = storeDesp;
	}
	public String getStoreDesp() {
		return storeDesp;
	}
	public void setmUnit(Unit mUnit) {
		this.mUnit = mUnit;
	}
	public Unit getmUnit() {
		return mUnit;
	}
	public void setmPrice(Float mPrice) {
		this.mPrice = mPrice;
	}
	public Float getmPrice() {
		return mPrice;
	}
	public void setStockStatus(Status stockStatus) {
		this.stockStatus = stockStatus;
	}
	public Status getStockStatus() {
		return stockStatus;
	}
	public void setMerchantise(ArrayList<String> merchantise) {
		this.merchantise = merchantise;
	}
	public ArrayList<String> getMerchantise() {
		return merchantise;
	}
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegion() {
        return region;
    }
    public void setDistance(Float distance) {
        this.distance = distance;
    }
    public Float getDistance() {
        return distance;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public String getHours() {
        return hours;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
    public void setLng(Float lng) {
        this.lng = lng;
    }
    public Float getLng() {
        return lng;
    }
    public void setLat(Float lat) {
        this.lat = lat;
    }
    public Float getLat() {
        return lat;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }
	
}
