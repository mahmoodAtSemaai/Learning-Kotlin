package com.webkul.mobikul.odoo.model.customer.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class OrderData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("canReorder")
    @Expose
    private boolean canReorder;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("shipAdd_url")
    @Expose
    private String shipAddUrl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount_total")
    @Expose
    private String amountTotal;

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public boolean isCanReorder() {
        return canReorder;
    }

    public String getCreateDate() {
        if (createDate == null) {
            return "";
        }

        return createDate;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public String getShippingAddress() {
        if (shippingAddress == null) {
            return "";
        }

        return shippingAddress;
    }

    public String getUrl() {
        if (url == null) {
            return "";
        }

        return url;
    }

    public String getShipAddUrl() {
        if (shipAddUrl == null) {
            return "";
        }

        return shipAddUrl;
    }

    public String getId() {
        if (id == null) {
            return "";
        }

        return id;
    }

    public String getAmountTotal() {
        if (amountTotal == null) {
            return "";
        }

        return amountTotal;
    }
}
