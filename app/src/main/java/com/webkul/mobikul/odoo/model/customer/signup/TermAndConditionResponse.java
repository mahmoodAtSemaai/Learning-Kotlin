package com.webkul.mobikul.odoo.model.customer.signup;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

public class TermAndConditionResponse extends BaseResponse {

    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName(value = "termsAndConditions",alternate = {"term_and_condition"})
    @Expose
    private String termsAndConditions;

    protected TermAndConditionResponse(Parcel in) {
        super(in);
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

}
