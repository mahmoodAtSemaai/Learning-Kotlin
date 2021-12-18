package com.webkul.mobikul.odoo.model.customer.address.addressResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.generic.StateData;
import com.webkul.mobikul.odoo.model.generic.SubDistrictData;
import com.webkul.mobikul.odoo.model.generic.VillageData;

import java.util.ArrayList;
import java.util.List;

public class VillageListResponse extends BaseResponse{

    private static final String TAG = "SubDistrictListResponse";

    @SerializedName(AddressAPIConstants.STATUS)
    @Expose
    public String status;

    @SerializedName(AddressAPIConstants.DATA)
    @Expose
    public ArrayList<VillageData> data;

    protected VillageListResponse(Parcel in) {
        super(in);
    }

    public VillageListResponse(Parcel in, String status, ArrayList<VillageData> data) {
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

    public ArrayList<VillageData> getData() {
        return data;
    }

    public void setData(ArrayList<VillageData> data) {
        this.data = data;
    }
}
