package com.webkul.mobikul.odoo.model.product;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

/**
 * Created by shubham.agarwal on 2/6/17.
 */

public class AddToCartResponse extends BaseResponse {

    @SuppressWarnings("unused")
    private static final String TAG = "AddToCartResponse";
    @SerializedName("productName")
    @Expose
    private String productName;


    protected AddToCartResponse(Parcel in) {
        super(in);
    }


    public String getProductName() {
        if (productName == null) {
            return "";
        }
        return productName;
    }
}
