package com.webkul.mobikul.odoo.model.customer.account;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

/**
 * Created by shubham.agarwal on 29/5/17.
 */

public class SaveCustomerDetailResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "SaveCustomerDetailRespo";


    @SerializedName("customerProfileImage")
    @Expose
    private String customerProfileImage;

    @SerializedName("customerBannerImage")
    @Expose
    private String customerBannerImage;

    public String getCustomerBannerImage() {
        return customerBannerImage;
    }

    public void setCustomerBannerImage(String customerBannerImage) {
        this.customerBannerImage = customerBannerImage;
    }

    protected SaveCustomerDetailResponse(Parcel in) {
        super(in);
    }

    public String getCustomerProfileImage() {
        if (customerProfileImage == null) {
            return "";
        }
        return customerProfileImage;
    }
}
