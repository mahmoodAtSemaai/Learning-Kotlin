package com.webkul.mobikul.odoo.model.customer.address;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressRequestBody {

    public static final String KEY_PROVINCE = "province";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_SUB_DISTRICT = "sub_district";
    public static final String KEY_VILLAGE = "village";
    private static final String TAG = "AddressRequestBody";
    private static final String KEY_NAME = "name";
    private static final String KEY_ZIP = "zip";
    private static final String KEY_STREET = "street";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_STATE_ID = "state_id";
    private static final String KEY_COUNTRY_ID = "country_id";

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    private static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_SUB_DISTRICT_ID = "sub_district_id";
    private static final String KEY_VILLAGE_ID = "village_id";
    private static final String KEY_SET_AS_DEFAUT_SHIPPING_ADDRESS = "setAsDefault";
    public String name;
    public String zip;
    public String street;
    public String phone;
    public String country_id;
    public String company_id;
    public String state_id;
    public String district_id;
    public String sub_district_id;
    public String village_id;
    public AddressRequestBody() {
    }

    public static String getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getSub_district_id() {
        return sub_district_id;
    }

    public void setSub_district_id(String sub_district_id) {
        this.sub_district_id = sub_district_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getNewAddressData() {
        JSONObject bodyParams = new JSONObject();
        try {
            bodyParams.put(KEY_NAME, getName());
            bodyParams.put(KEY_PHONE, getPhone());
            bodyParams.put(KEY_STREET, getStreet());
            bodyParams.put(KEY_ZIP, getZip());
            bodyParams.put(KEY_COUNTRY_ID, getCountry_id());
            bodyParams.put(KEY_STATE_ID, getState_id());
            bodyParams.put(KEY_DISTRICT_ID, getDistrict_id());
            bodyParams.put(KEY_SUB_DISTRICT_ID, getSub_district_id());
            bodyParams.put(KEY_VILLAGE_ID, getVillage_id());
//            if (getAddressType() != NewAddressFragment.AddressType.TYPE_BILLING && getAddressType() != NewAddressFragment.AddressType.TYPE_SHIPPING) {
//            }
//            addressFormDataJson.put(KEY_SET_AS_DEFAUT_SHIPPING_ADDRESS, isSetAsDefaultShippingAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getNewAddressData: " + bodyParams.toString());
        return bodyParams.toString();
    }
}
