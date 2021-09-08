package com.webkul.mobikul.odoo.model.extra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aastha.gupta on 13/7/17.
 */

public class AddOns implements Parcelable {
    public static final Creator<AddOns> CREATOR = new Creator<AddOns>() {
        @Override
        public AddOns createFromParcel(Parcel in) {
            return new AddOns(in);
        }

        @Override
        public AddOns[] newArray(int size) {
            return new AddOns[size];
        }
    };
    @SerializedName("review")
    @Expose
    private boolean reviewModuleInstalled;
    @SerializedName("wishlist")
    @Expose
    private boolean wishlistModuleInstalled;
    @SerializedName("email_verification")
    @Expose
    private boolean emailVerification;
    @SerializedName("odoo_marketplace")
    @Expose
    private boolean odooMarketplace;

    @SerializedName("odoo_gdpr")
    @Expose
    private boolean odooGdpr;

    public AddOns(Parcel in) {
        reviewModuleInstalled = in.readByte() != 0;
        wishlistModuleInstalled = in.readByte() != 0;
        emailVerification = in.readByte() != 0;
        odooMarketplace = in.readByte() != 0;
        odooGdpr = in.readByte() != 0;
    }

    public boolean isReviewModuleInstalled() {
        return reviewModuleInstalled;
    }

    public boolean isWishlistModuleInstalled() {
        return wishlistModuleInstalled;
    }

    public boolean isEmailVerification() {
        return emailVerification;
    }

    public boolean isOdooMarketplace() {
        return odooMarketplace;
    }

    public boolean isOdooGdpr() {
        return odooGdpr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (reviewModuleInstalled ? 1 : 0));
        dest.writeByte((byte) (wishlistModuleInstalled ? 1 : 0));
        dest.writeByte((byte) (emailVerification ? 1 : 0));
        dest.writeByte((byte) (odooMarketplace ? 1 : 0));
        dest.writeByte((byte) (odooGdpr ? 1 : 0));
    }
}
