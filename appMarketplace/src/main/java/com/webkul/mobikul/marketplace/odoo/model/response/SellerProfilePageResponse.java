package com.webkul.mobikul.marketplace.odoo.model.response;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.marketplace.odoo.model.SellerInfo;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 21/9/17.
 */

public class SellerProfilePageResponse extends BaseResponse {
    @SerializedName("SellerInfo")
    @Expose
    private SellerInfo sellerInfo;
    @SerializedName("wishlists")
    @Expose
    private List<Integer> wishlists = null;

    protected SellerProfilePageResponse(Parcel in) {
        super(in);
        sellerInfo = in.readParcelable(SellerInfo.class.getClassLoader());
    }

    public SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public List<Integer> getWishlists() {
        return wishlists;
    }
}
