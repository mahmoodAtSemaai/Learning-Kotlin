package com.webkul.mobikul.marketplace.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

/**
 * Created by aastha.gupta on 20/2/18 in Mobikul_Odoo_Application.
 */

public class SellerDashboardResponse extends BaseResponse {

    @SerializedName("sellerDashboard")
    @Expose
    private SellerDashboard sellerDashboard;

    protected SellerDashboardResponse(Parcel in) {
        super(in);
    }

    public SellerDashboard getSellerDashboard() {
        return sellerDashboard;
    }
}
