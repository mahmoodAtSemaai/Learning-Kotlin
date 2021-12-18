package com.webkul.mobikul.odoo.model.customer.address.addressResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.generic.DistrictData;
import com.webkul.mobikul.odoo.model.generic.SubDistrictData;

import java.util.ArrayList;
import java.util.List;

public class SubDistrictListResponse extends BaseResponse {

    private static final String TAG = "SubDistrictListResponse";

    @SerializedName(AddressAPIConstants.STATUS)
    @Expose
    public String status;

    @SerializedName(AddressAPIConstants.DATA)
    @Expose
    public ArrayList<SubDistrictData> data;


    protected SubDistrictListResponse(Parcel in) {
        super(in);
    }

    public SubDistrictListResponse(Parcel in, String status, ArrayList<SubDistrictData> data) {
        super(in);
        this.status = status;
        this.data = data;
    }

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
