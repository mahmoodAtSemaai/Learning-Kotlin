package com.webkul.mobikul.odoo.model.checkout;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 25/5/17.
 */

public class PaymentAcquirerResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "PaymentAcquirerResponse";
    @SerializedName("acquirers")
    @Expose
    private List<PaymentAcquirerData> paymentAcquirers;


    protected PaymentAcquirerResponse(Parcel in) {
        super(in);
    }

    public List<PaymentAcquirerData> getPaymentAcquirers() {
        if (paymentAcquirers == null) {
            return new ArrayList<>();
        }
        return paymentAcquirers;
    }


}
