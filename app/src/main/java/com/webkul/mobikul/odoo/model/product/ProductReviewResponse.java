package com.webkul.mobikul.odoo.model.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aastha.gupta on 25/7/17.
 */

public class ProductReviewResponse {
    @SerializedName("reviewCount")
    @Expose
    private int reviewCount;
    @SerializedName("product_reviews")
    @Expose
    private List<Review> productReviews = null;
    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public List<Review> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<Review> productReviews) {
        this.productReviews = productReviews;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }
}
