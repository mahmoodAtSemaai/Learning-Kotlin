package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

/**
 * Created by shubham.agarwal on 26/5/17.
 */

public class OrderPlaceResponse extends BaseResponse {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;

    protected OrderPlaceResponse(Parcel in) {
        super(in);
    }

    public String getName() {
        if (name == null) {
            return "";
        }
        return name;
    }


    public String getUrl() {
        if (url == null) {
            return "";
        }
        return url;
    }
}
