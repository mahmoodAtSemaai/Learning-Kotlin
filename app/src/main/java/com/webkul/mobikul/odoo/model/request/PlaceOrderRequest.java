package com.webkul.mobikul.odoo.model.request;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubham.agarwal on 24/5/17.
 */

public class PlaceOrderRequest {
    @SuppressWarnings("unused")
    private static final String TAG = "PlaceOrderRequest";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_PAYMENT_REFERENCE = "paymentReference";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_PAYMENT_STATUS = "paymentStatus";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_TRANSACTION_ID = "transaction_id";
    @SuppressWarnings("FieldCanBeLocal")
    private final String KEY_USE_POINTS = "use_points";


    private final String paymentReference;
    private final String paymentStatus;
    private final int transactionId;
    private final Boolean usePoints;

    public PlaceOrderRequest(String paymentReference, String paymentStatus, int transactionId, Boolean usePoints) {
        this.paymentReference = paymentReference;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.usePoints = usePoints;
    }


    public PlaceOrderRequest(int transactionId, Boolean usePoints) {
        paymentReference = "";
        paymentStatus = "";
        this.usePoints = usePoints;
        this.transactionId = transactionId;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPaymentReference() {
        if (paymentReference == null) {
            return "";
        }
        return paymentReference;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPaymentStatus() {
        if (paymentStatus == null) {
            return "";
        }
        return paymentStatus;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Boolean getUsePoints() {
        return usePoints;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {

            if (!TextUtils.isEmpty(getPaymentReference())){
                jsonObject.put(KEY_PAYMENT_REFERENCE, getPaymentReference());
            }

            if (!TextUtils.isEmpty(getPaymentStatus())){
                jsonObject.put(KEY_PAYMENT_STATUS, getPaymentStatus());
            }

            if (getTransactionId() != 0){
                jsonObject.put(KEY_TRANSACTION_ID, getTransactionId());
            }
            jsonObject.put(KEY_USE_POINTS, getUsePoints());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
