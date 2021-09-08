package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.List;

/**
 * Created by aastha.gupta on 21/9/17.
 */

public class SellerProducts {

    @SerializedName("tcount")
    @Expose
    private int tcount;
    @SerializedName("products")
    @Expose
    private List<ProductData> products = null;
    @SerializedName("offset")
    @Expose
    private int offset;

    public int getTcount() {
        return tcount;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public int getOffset() {
        return offset;
    }
}
