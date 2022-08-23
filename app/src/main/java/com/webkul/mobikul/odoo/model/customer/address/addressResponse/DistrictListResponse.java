package com.webkul.mobikul.odoo.model.customer.address.addressResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.generic.DistrictData;
import com.webkul.mobikul.odoo.model.generic.StateData;

import java.util.ArrayList;
import java.util.List;

public class DistrictListResponse {

    private static final String TAG = "DistrictListResponse";

    @SerializedName(AddressAPIConstants.STATUS)
    @Expose
    public String status;

    @SerializedName(AddressAPIConstants.DATA)
    @Expose
    public ArrayList<DistrictData> data;

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

    public ArrayList<DistrictData> getData() {
        return data;
    }

    public void setData(ArrayList<DistrictData> data) {
        this.data = data;
    }
}
