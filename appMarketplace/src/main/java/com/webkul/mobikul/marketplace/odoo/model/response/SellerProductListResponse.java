package com.webkul.mobikul.marketplace.odoo.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.marketplace.odoo.model.SellerProductListItem;

import java.util.List;

/**
 * Created by aastha.gupta on 23/2/18 in Mobikul_Odoo_Application.
 */

public class SellerProductListResponse {

    @SerializedName("sellerProduct")
    @Expose
    private List<SellerProductListItem> sellerProducts = null;
    @SerializedName("tcount")
    @Expose
    private int tcount;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;

    private boolean lazyLoading = false;


    public List<SellerProductListItem> getSellerProducts() {
        return sellerProducts;
    }

    public int getTcount() {
        return tcount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isLazyLoading() {
        return lazyLoading;
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }
}
