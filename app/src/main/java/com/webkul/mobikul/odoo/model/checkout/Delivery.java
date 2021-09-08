package com.webkul.mobikul.odoo.model.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aastha.gupta on 19/3/18 in Mobikul_Odoo_Application.
 */

public class Delivery {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("shippingId")
    @Expose
    private int shippingId;
    @SerializedName("tax")
    @Expose
    private List<String> tax = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;

    public String getTotal() {
        return total;
    }

    public int getShippingId() {
        return shippingId;
    }

    public List<String> getTax() {
        return tax;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
