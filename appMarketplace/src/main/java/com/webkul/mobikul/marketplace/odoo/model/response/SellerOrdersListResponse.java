package com.webkul.mobikul.marketplace.odoo.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.marketplace.odoo.model.SellerOrderLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aastha.gupta on 23/2/18 in Mobikul_Odoo_Application.
 */

public class SellerOrdersListResponse {
    @SerializedName("tcount")
    @Expose
    private int tcount;
    @SerializedName("sellerOrderLines")
    @Expose
    private List<SellerOrderLine> sellerOrderLines = new ArrayList<>();
    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;

    private boolean lazyLoading = false;

    private int offset = 0;

    public int getTcount() {
        return tcount;
    }

    public List<SellerOrderLine> getSellerOrderLines() {
        return sellerOrderLines;
    }

    public boolean isLazyLoading() {
        return lazyLoading;
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }
}
