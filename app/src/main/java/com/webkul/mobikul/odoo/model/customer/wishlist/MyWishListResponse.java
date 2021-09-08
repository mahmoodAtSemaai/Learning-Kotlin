package com.webkul.mobikul.odoo.model.customer.wishlist;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 10/7/17.
 */

public class MyWishListResponse extends BaseResponse {


    @SerializedName("WishlistCount")
    @Expose
    private int wishlistCount;
    @SerializedName("wishLists")
    @Expose
    private List<WishListData> wishLists = null;

    protected MyWishListResponse(Parcel in) {
        super(in);
    }

    public int getWishlistCount() {
        return wishlistCount;
    }

    public void setWishlistCount(int wishlistCount) {
        this.wishlistCount = wishlistCount;
    }

    public List<WishListData> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<WishListData> wishLists) {
        this.wishLists = wishLists;
    }
}
