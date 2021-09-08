package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 23/2/18 in Mobikul_Odoo_Application.
 */

public class SellerProductListItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("seller")
    @Expose
    private String seller;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("templateId")
    @Expose
    private int templateId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;

    public String getName() {
        return name;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public String getSeller() {
        return seller;
    }

    public String getState() {
        return state;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getQty() {
        return qty;
    }

    public String getThumbNail() {
        return thumbNail;
    }
}
