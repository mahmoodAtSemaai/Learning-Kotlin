package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 20/2/18 in Mobikul_Odoo_Application.
 */

class OrderAmount {

    @SerializedName("value")
    @Expose
    private float value;
    @SerializedName("label")
    @Expose
    private String label;
}
