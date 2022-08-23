package com.webkul.mobikul.odoo.model.customer.address.addressResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.generic.SubDistrictData;

import java.util.ArrayList;

public class SubDistrictListResponse{

    private static final String TAG = "SubDistrictListResponse";

    @SerializedName(AddressAPIConstants.STATUS)
    @Expose
    public String status;

    @SerializedName(AddressAPIConstants.DATA)
    @Expose
    public ArrayList<SubDistrictData> data;

    @SerializedName(AddressAPIConstants.MESSAGE)
    @Expose
    public String message;

    public static String getTAG() {
        return TAG;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SubDistrictData> getData() {
        return data;
    }

    public void setData(ArrayList<SubDistrictData> data) {
        this.data = data;
    }
}
