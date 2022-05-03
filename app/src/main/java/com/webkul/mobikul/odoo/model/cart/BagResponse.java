package com.webkul.mobikul.odoo.model.cart;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.generic.KeyValuePairData;

import java.util.ArrayList;
import java.util.List;


public class BagResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "CartDetailsResponse";
    @SerializedName("grandtotal")
    @Expose
    private KeyValuePairData grandTotal;
    @SerializedName("tax")
    @Expose
    private KeyValuePairData tax;
    @SerializedName("subtotal")
    @Expose
    private KeyValuePairData subtotal;
    @SerializedName("items")
    @Expose
    private List<BagItemData> items = null;
    /*name => order_reference*/
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pointsRedeemed")
    @Expose
    private KeyValuePairData pointsRedeemed;
    @SerializedName("order_id")
    @Expose
    private int orderId;


    protected BagResponse(Parcel in) {
        super(in);
    }

    public KeyValuePairData getGrandTotal() {
        if (grandTotal == null) {
            return new KeyValuePairData(null);
        }
        return grandTotal;
    }

    public KeyValuePairData getTax() {
        if (tax == null) {
            return new KeyValuePairData(null);
        }

        return tax;
    }

    public KeyValuePairData getSubtotal() {
        if (subtotal == null) {
            return new KeyValuePairData(null);
        }

        return subtotal;
    }

    public KeyValuePairData getPointsRedeemed() {
        return pointsRedeemed;
    }

    public List<BagItemData> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public int getOrderId() {
        return orderId;
    }
}