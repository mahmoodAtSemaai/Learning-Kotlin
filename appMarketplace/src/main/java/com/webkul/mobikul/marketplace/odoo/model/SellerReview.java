package com.webkul.mobikul.marketplace.odoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 21/9/17.
 */

public class SellerReview {

    @SerializedName("customer_profile_image")
    @Expose
    private String customerProfileImage;

    //    @SerializedName("rating2")
//    @Expose
//    private int rating2;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("helpful")
    @Expose
    private String helpful;
    @SerializedName("message_is_follower")
    @Expose
    private boolean messageIsFollower;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("not_helpful")
    @Expose
    private String notHelpful;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("total_votes")
    @Expose
    private int totalVotes;

//    public int getRating2() {
//        return rating2;
//    }
//
//    public void setRating2(int rating2) {
//        this.rating2 = rating2;
//    }

    public boolean isMessageIsFollower() {
        return messageIsFollower;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getHelpful() {
        return helpful;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public String getNotHelpful() {
        return notHelpful;
    }

    public String getEmail() {
        return email;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public String getCustomerProfileImage() {
        return customerProfileImage;
    }

    public String getName() {
        return name;
    }

    public void setHelpful(String helpful) {
        this.helpful = helpful;
    }

    public void setNotHelpful(String notHelpful) {
        this.notHelpful = notHelpful;
    }
}
