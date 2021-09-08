package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.cart.BagItemData;
import com.webkul.mobikul.odoo.model.generic.KeyValuePairData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 26/5/17.
 */

public class OrderReviewResponse extends BaseResponse implements Parcelable {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderReviewResponse";

    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("grandtotal")
    @Expose
    private KeyValuePairData grandTotal;
    @SerializedName("billingAddress")
    @Expose
    private String billingAddress;
    @SerializedName("items")
    @Expose
    private List<BagItemData> items = null;
    @SerializedName("subtotal")
    @Expose
    private KeyValuePairData subtotal;
    @SerializedName("tax")
    @Expose
    private KeyValuePairData tax;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paymentData")
    @Expose
    private PaymentAcquirerData paymentAcquirerData;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("paymentAcquirer")
    @Expose
    private String paymentAcquirer;

    @SerializedName("delivery")
    @Expose
    private Delivery delivery;

    @SerializedName("paymentTerms")
    @Expose
    private paymentTermsNCondition paymentTerms;

    @SerializedName("transaction_id")
    @Expose
    private int transactionId;


    protected OrderReviewResponse(Parcel in) {
        super(in);
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public String getShippingAddress() {
        if (shippingAddress == null) {
            return "";
        }
        return shippingAddress;
    }

    public KeyValuePairData getGrandTotal() {
        if (grandTotal == null) {
            return new KeyValuePairData(null);
        }

        return grandTotal;
    }

    public String getBillingAddress() {
        if (billingAddress == null) {
            return "";
        }

        return billingAddress;
    }

    public List<BagItemData> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }

        return items;
    }

    public KeyValuePairData getSubtotal() {
        if (subtotal == null) {
            return new KeyValuePairData(null);
        }

        return subtotal;
    }

    public KeyValuePairData getTax() {
        if (tax == null) {
            return new KeyValuePairData(null);
        }

        return tax;
    }

    public String getName() {
        if (name == null) {
            return "";
        }

        return name;
    }

    public PaymentAcquirerData getPaymentAcquirerData() {
        if (paymentAcquirerData == null) {
            return new PaymentAcquirerData(null);
        }
        return paymentAcquirerData;
    }


    public double getAmount() {
        return amount * 100;
    }

    public String getCurrency() {
        if (currency == null) {
            return "";
        }
        return currency;
    }

    public String getPaymentAcquirer() {
        if (paymentAcquirer == null) {
            return "";
        }
        return paymentAcquirer;
    }

    public paymentTermsNCondition getPaymentTerms() {
        return paymentTerms;
    }

    public int getTransactionId() {
        return transactionId;
    }


    public boolean showTermsNCondition() {
        return paymentTerms != null && (!TextUtils.isEmpty(paymentTerms.getPaymentShortTerms()) || !TextUtils.isEmpty(paymentTerms.getPaymentLongTerms()));
    }

    public boolean hasPaymentShortTerms() {
        return paymentTerms != null && !TextUtils.isEmpty(paymentTerms.getPaymentShortTerms());
    }


}
