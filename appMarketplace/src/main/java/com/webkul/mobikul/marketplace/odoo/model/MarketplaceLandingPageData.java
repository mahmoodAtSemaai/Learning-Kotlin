package com.webkul.mobikul.marketplace.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class MarketplaceLandingPageData extends BaseResponse {
    @SerializedName("heading")
    @Expose
    public String heading;
    @SerializedName("banner")
    @Expose
    public String banner;
    @SerializedName("SellersDetail")
    @Expose
    public List<SellerInfo> sellersDetail = null;

    protected MarketplaceLandingPageData(Parcel in) {
        super(in);
    }

    public String getHeading() {
        return heading;
    }

    public String getBanner() {
        return banner;
    }

    public List<SellerInfo> getSellersDetail() {
        return sellersDetail;
    }
}
