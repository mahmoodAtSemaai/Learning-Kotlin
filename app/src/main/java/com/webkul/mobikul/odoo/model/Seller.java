package com.webkul.mobikul.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 19/9/17.
 */

public class Seller {

    @SerializedName("average_rating")
    @Expose
    private float averageRating;
    @SerializedName("marketplace_seller_id")
    @Expose
    private String marketplaceSellerId;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("total_reviews")
    @Expose
    private int totalReviews;
    @SerializedName("seller_profile_image")
    @Expose
    private String sellerProfileImage;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("seller_profile_url")
    @Expose
    private String sellerProfileUrl;
    @SerializedName("icon")
    @Expose
    private String icon;

    public float getAverageRating() {
        return averageRating;
    }

    public String getMarketplaceSellerId() {
        return marketplaceSellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public String getSellerProfileImage() {
        return sellerProfileImage;
    }

    public String getMessage() {
        return message;
    }

    public String getSellerProfileUrl() {
        return sellerProfileUrl;
    }

    public String getIcon() {
        return icon;
    }
}
