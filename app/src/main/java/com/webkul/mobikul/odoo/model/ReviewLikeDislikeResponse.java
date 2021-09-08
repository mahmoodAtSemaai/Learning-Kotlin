package com.webkul.mobikul.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewLikeDislikeResponse extends BaseResponse{

    @SerializedName("WishlistCount")
    @Expose
    private Integer wishlistCount;
    @SerializedName("is_seller")
    @Expose
    private Boolean isSeller;
    @SerializedName("SellerReview")
    @Expose
    private List<SellerLikeUnLikeModel> sellerReview = null;

    protected ReviewLikeDislikeResponse(Parcel in) {
        super(in);
    }

    public Integer getWishlistCount() {
        return wishlistCount;
    }

    public void setWishlistCount(Integer wishlistCount) {
        this.wishlistCount = wishlistCount;
    }

    public Boolean getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(Boolean isSeller) {
        this.isSeller = isSeller;
    }

    public SellerLikeUnLikeModel getSellerReview() {
        if(sellerReview!=null && sellerReview.size()>0)
            return sellerReview.get(0);
        return null;
    }

    public void setSellerReview(List<SellerLikeUnLikeModel> sellerReview) {
        this.sellerReview = sellerReview;
    }


}
