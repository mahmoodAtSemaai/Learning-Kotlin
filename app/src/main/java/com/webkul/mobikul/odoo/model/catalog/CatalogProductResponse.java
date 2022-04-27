package com.webkul.mobikul.odoo.model.catalog;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BR;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 8/5/17.
 */

public class CatalogProductResponse extends BaseObservable {
    @SerializedName("products")
    @Expose
    private ArrayList<ProductData> mProducts;
    @SerializedName("limit")
    @Expose
    private int limit;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("tcount")
    @Expose
    private int totalCount;

    @SerializedName("wishlist")
    @Expose
    private List<String> wishlist;
    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;


    private boolean lazyLoading;

    public ArrayList<ProductData> getProducts() {
        if (mProducts == null) {
            return new ArrayList<>();
        }
        return mProducts;
    }

    public List<String> getWishlist() {
        if (wishlist == null) {
            return new ArrayList<>();
        }
        return wishlist;
    }

    public void setWishlistData() {
        if (wishlist != null) {
            for (String item : wishlist) {
                for (ProductData data : getProducts()) {
                    if (data.getProductId().equals(item)) {
                        data.setAddedToWishlist(true);
                        break;
                    }
                }
            }
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalCount() {
        return totalCount;
    }

    @Bindable
    public boolean isLazyLoading() {
        return lazyLoading;
    }

    public void setLazyLoading(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
        notifyPropertyChanged(BR.lazyLoading);
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

}
