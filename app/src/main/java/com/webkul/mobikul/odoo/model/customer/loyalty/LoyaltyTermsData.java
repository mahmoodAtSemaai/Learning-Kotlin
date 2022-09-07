package com.webkul.mobikul.odoo.model.customer.loyalty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URL;

import static com.webkul.mobikul.odoo.helper.DateUtilKt.changeFormat;

public class LoyaltyTermsData {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("points")
    @Expose
    private String points;
    @SerializedName("expiration_date")
    @Expose
    private String expiry;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("how_to_use")
    @Expose
    private String howToUse;
    @SerializedName("terms_title")
    @Expose
    private String termsTitle;
    @SerializedName("how_to_use_title")
    @Expose
    private String howToUseTitle;
    @SerializedName("lang_code")
    @Expose
    private String langCode;


    public int getId() {
        return id;
    }

    public String getPoints() {
        if (points == null) {
            return "";
        }
        return points;
    }

    public String getPointsWithSign() {
        if (points == null) {
            return "";
        }
        return "+" + points;
    }

    public String getExpiry() {
        if (expiry == null) {
            return "";
        }
        return changeFormat(langCode, expiry);
    }

    public String getImageUrl() {
        if (imageUrl == null) {
            return "";
        }
        return imageUrl;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public String getSubTitle() {
        if (subTitle == null) {
            return "";
        }
        return subTitle;
    }

    public String getTermsTitle() {
        if (termsTitle == null) {
            return "";
        }
        return termsTitle;
    }

    public String getHowToUseTitle() {
        if (howToUseTitle == null) {
            return "";
        }
        return howToUseTitle;
    }

    public String getTerms() {
        if (terms == null) {
            return "";
        }
        return terms;
    }

    public String getHowToUse() {
        if (howToUse == null) {
            return "";
        }
        return howToUse;
    }

    public String getLangCode() {
        if (langCode == null) {
            return "";
        }
        return langCode;
    }

}
