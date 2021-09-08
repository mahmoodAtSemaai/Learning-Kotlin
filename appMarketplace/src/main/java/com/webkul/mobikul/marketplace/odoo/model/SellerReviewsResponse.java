package com.webkul.mobikul.marketplace.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class SellerReviewsResponse extends BaseResponse {
    @SerializedName("SellerReview")
    @Expose
    public List<SellerReview> sellerReview = null;

    @SerializedName("sellerReviewCount")
    @Expose
    public int sellerReviewCount;

    protected SellerReviewsResponse(Parcel in) {
        super(in);
    }

    public List<SellerReview> getSellerReview() {
        return sellerReview;
    }

    public int getSellerReviewCount() {
        return sellerReviewCount;
    }
}
