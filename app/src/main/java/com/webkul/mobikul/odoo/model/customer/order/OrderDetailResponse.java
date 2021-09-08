package com.webkul.mobikul.odoo.model.customer.order;

import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.checkout.Delivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class OrderDetailResponse extends BaseResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("billing_address")
    @Expose
    private String billingAddress;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shipAdd_url")
    @Expose
    private String shipAddUrl;
    @SerializedName("amount_tax")
    @Expose
    private String amountTax;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("amount_untaxed")
    @Expose
    private String amountUntaxed;
    @SerializedName("items")
    @Expose
    private List<OrderItem> items = null;
    @SerializedName("amount_total")
    @Expose
    private String amountTotal;
    @SerializedName("delivery")
    @Expose
    private Delivery delivery;
    @SerializedName("deliveryOrders")
    @Expose
    private ArrayList<DeliveryOrder> deliveryOrders = null;
    @SerializedName("transactions")
    @Expose
    private ArrayList<OrderDetailTransaction> transactions = null;



    protected OrderDetailResponse(@Nullable Parcel in) {
        super(in);
    }

    public String getStatus() {
        if (status == null) {
            return "";
        }
        return status;
    }

    public String getBillingAddress() {
        if (billingAddress == null) {
            return "";
        }
        return billingAddress;
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

    public String getShipAddUrl() {
        if (shipAddUrl == null) {
            return "";
        }
        return shipAddUrl;
    }

    public String getAmountTax() {
        if (amountTax == null) {
            return "";
        }
        return amountTax;
    }

    public String getShippingAddress() {
        if (shippingAddress == null) {
            return "";
        }
        return shippingAddress;
    }

    public String getAmountUntaxed() {
        if (amountUntaxed == null) {
            return "";
        }
        return amountUntaxed;
    }

    public List<OrderItem> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public String getAmountTotal() {
        if (amountTotal == null) {
            return "";
        }
        return amountTotal;
    }

    public Delivery getDelivery() {
        return delivery;
    }


    public ArrayList<DeliveryOrder> getDeliveryOrders() {
        if (deliveryOrders == null){
            deliveryOrders = new ArrayList<>();
        }
        return deliveryOrders;
    }


    public ArrayList<OrderDetailTransaction> getTransactions() {
        if (transactions == null){
            transactions = new ArrayList<>();
        }
        return transactions;
    }
}
