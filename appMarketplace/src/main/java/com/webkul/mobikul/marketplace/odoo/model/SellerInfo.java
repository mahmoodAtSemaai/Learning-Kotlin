package com.webkul.mobikul.marketplace.odoo.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

/**
 * Created by aastha.gupta on 19/9/17.
 */

public class SellerInfo extends BaseResponse {
    @SerializedName("shipping_policy")
    @Expose
    private String shippingPolicy;
    @SerializedName("seller_id")
    @Expose
    private int sellerId;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("seller_profile_image")
    @Expose
    private String sellerProfileImage;
    @SerializedName("country")
    @Expose
    private String countryId;
    @SerializedName("state")
    @Expose
    private String stateId;
    @SerializedName("return_policy")
    @Expose
    private String returnPolicy;
    @SerializedName("seller_profile_banner")
    @Expose
    private String sellerProfileBanner;
    @SerializedName("average_rating")
    @Expose
    private float averageRating;
    @SerializedName("profile_msg")
    @Expose
    private String profileMsg;
    @SerializedName("sales_count")
    @Expose
    private int salesCount;
    @SerializedName("product_count")
    @Expose
    private int productCount;
    @SerializedName("total_reviews")
    @Expose
    private int totalReviews;
    @SerializedName("sellerProducts")
    @Expose
    private SellerProducts sellerProducts;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("seller_reviews")
    @Expose
    private List<SellerReview> sellerReviews = null;

    protected SellerInfo(Parcel in) {
        super(in);
    }

    public String getShippingPolicy() {
        return shippingPolicy;
    }

    public int getSellerId() {
        return sellerId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getName() {
        return name;
    }

    public String getSellerProfileImage() {
        return sellerProfileImage;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public String getSellerProfileBanner() {
        return sellerProfileBanner;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public String getProfileMsg() {
        if (profileMsg == null) {
            return "";
        }
        return profileMsg;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public SellerProducts getSellerProducts() {
        return sellerProducts;
    }

    public String getEmail() {
        return email;
    }

    public List<SellerReview> getSellerReviews() {
        return sellerReviews;
    }
}
