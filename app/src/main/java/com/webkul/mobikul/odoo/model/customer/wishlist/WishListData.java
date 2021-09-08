package com.webkul.mobikul.odoo.model.customer.wishlist;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 10/7/17.
 */

public class WishListData {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("priceUnit")
    @Expose
    private String priceUnit;
    @SerializedName("priceReduce")
    @Expose
    private String priceReduce;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumbNail")
    @Expose
    private String thumbNail;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("templateId")
    @Expose
    private String templateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPriceReduce() {
        return priceReduce;
    }

    public void setPriceReduce(String priceReduce) {
        this.priceReduce = priceReduce;
    }
}
