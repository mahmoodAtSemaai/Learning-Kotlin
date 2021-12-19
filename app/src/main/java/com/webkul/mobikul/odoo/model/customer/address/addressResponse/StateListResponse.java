package com.webkul.mobikul.odoo.model.customer.address.addressResponse;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.generic.StateData;

import java.util.ArrayList;

public class StateListResponse extends BaseResponse{

    private static final String TAG = "StateListResponse";

    @SerializedName(AddressAPIConstants.STATUS)
    @Expose
    public String status;

    @SerializedName(AddressAPIConstants.DATA)
    @Expose
    public ArrayList<StateData> data;

    public StateListResponse(Parcel in, ArrayList<StateData> data) {
        super(in);
        this.data = data;
    }

    public static String getTAG() {
        return TAG;
    }

    public ArrayList<StateData> getResult() {
        return data;
    }

    public void setResult(ArrayList<StateData> result) {
        this.data = result;
    }

    protected StateListResponse(Parcel in) {
        super(in);
    }
}
