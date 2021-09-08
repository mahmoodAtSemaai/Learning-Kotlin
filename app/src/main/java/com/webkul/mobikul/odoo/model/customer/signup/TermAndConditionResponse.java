package com.webkul.mobikul.odoo.model.customer.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermAndConditionResponse {

    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName(value = "termsAndConditions",alternate = {"term_and_condition"})
    @Expose
    private String termsAndConditions;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("itemsPerPage")
    @Expose
    private Integer itemsPerPage;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    //    @SerializedName("addons")
//    @Expose
//    private Addons addons;
    @SerializedName("pricelist")
    @Expose
    private Integer pricelist;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//        public Addons getAddons() {
//            return addons;
//        }
//
//        public void setAddons(Addons addons) {
//            this.addons = addons;
//        }

    public Integer getPricelist() {
        return pricelist;
    }

    public void setPricelist(Integer pricelist) {
        this.pricelist = pricelist;
    }

}
