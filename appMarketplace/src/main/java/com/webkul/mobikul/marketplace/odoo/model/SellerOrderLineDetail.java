package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 1/3/18 in Mobikul_Odoo_Application.
 */

public class SellerOrderLineDetail {
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
    private float deliveredQty;
    @SerializedName("order_payment_acquirer")
    @Expose
    private String orderPaymentAcquirer;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("delivery_method")
    @Expose
    private String deliveryMethod;
    @SerializedName("order_reference")
    @Expose
    private int orderReference;
    @SerializedName("marketplace_state")
    @Expose
    private String marketplaceState;
    @SerializedName("quantity")
    @Expose
    private float quantity;

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

    public float getDeliveredQty() {
        return deliveredQty;
    }

    public String getOrderPaymentAcquirer() {
        return orderPaymentAcquirer;
    }

    public String getCustomer() {
        return customer;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public int getOrderReference() {
        return orderReference;
    }

    public String getMarketplaceState() {
        return marketplaceState;
    }

    public float getQuantity() {
        return quantity;
    }
}
