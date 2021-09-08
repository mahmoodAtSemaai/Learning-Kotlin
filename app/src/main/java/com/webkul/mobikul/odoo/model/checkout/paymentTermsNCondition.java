package com.webkul.mobikul.odoo.model.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class paymentTermsNCondition {

    @SerializedName("paymentShortTerms")
    @Expose
    private String paymentShortTerms;
    @SerializedName("paymentLongTerms")
    @Expose
    private String paymentLongTerms;

    public String getPaymentShortTerms() {
        return paymentShortTerms;
    }

    public String getPaymentLongTerms() {
        return paymentLongTerms;
    }


}
