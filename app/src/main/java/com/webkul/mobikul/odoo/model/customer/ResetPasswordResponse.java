package com.webkul.mobikul.odoo.model.customer;

import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

/**
 * Created by shubham.agarwal on 9/5/17.
 */

public class ResetPasswordResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "ResetPasswordResponse";
    @SerializedName("productPerPage")
    @Expose
    private int productPerPage;


    protected ResetPasswordResponse(@Nullable Parcel in) {
        super(in);
    }

    public int getProductPerPage() {
        return productPerPage;
    }
}
