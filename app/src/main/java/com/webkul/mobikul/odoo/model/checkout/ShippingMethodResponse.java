package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 19/3/18 in Mobikul_Odoo_Application.
 */

public class ShippingMethodResponse extends BaseResponse {
    @SerializedName("ShippingMethods")
    @Expose
    private List<ActiveShippingMethod> shippingMethods = null;

    protected ShippingMethodResponse(Parcel in) {
        super(in);
    }

    public List<ActiveShippingMethod> getShippingMethods() {
        return shippingMethods;
    }
}
