package com.webkul.mobikul.odoo.model.request;

import android.text.TextUtils;

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


    private final String mPaymentReference;
    private final String mPaymentStatus;
    private final int mTransactionId;


//    public PlaceOrderRequest(String paymentReference, String paymentStatus) {
//        mPaymentReference = paymentReference;
//        mPaymentStatus = paymentStatus;
//    }

    public PlaceOrderRequest(String paymentReference, String paymentStatus, int transactionId) {
        this.mPaymentReference = paymentReference;
        this.mPaymentStatus = paymentStatus;
        this.mTransactionId = transactionId;
    }


    public PlaceOrderRequest(int transactionId) {
        mPaymentReference = "";
        mPaymentStatus = "";
        mTransactionId = transactionId;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPaymentReference() {
        if (mPaymentReference == null) {
            return "";
        }
        return mPaymentReference;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPaymentStatus() {
        if (mPaymentStatus == null) {
            return "";
        }
        return mPaymentStatus;
    }

    public int getTransactionId() {
        return mTransactionId;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
