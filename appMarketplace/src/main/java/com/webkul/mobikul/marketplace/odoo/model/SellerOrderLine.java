package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 23/2/18 in Mobikul_Odoo_Application.
 */

public class SellerOrderLine {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price_unit")
    @Expose
    private String priceUnit;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("line_id")
    @Expose
    private int lineId;
    @SerializedName("order_state")
    @Expose
    private String orderState;
    @SerializedName("delivered_qty")
    @Expose
    private int deliveredQty;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("order_reference")
    @Expose
    private String orderReference;
    @SerializedName("quantity")
    @Expose
    private int quantity;

    public String getProduct() {
        return product;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getDescription() {
        return description;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public int getLineId() {
        return lineId;
    }

    public String getOrderState() {
        return orderState;
    }

    public int getDeliveredQty() {
        return deliveredQty;
    }

    public String getCustomer() {
        return customer;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public int getQuantity() {
        return quantity;
    }
}
