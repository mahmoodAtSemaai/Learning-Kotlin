package com.webkul.mobikul.odoo.model;

import androidx.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.extra.AddOns;

/**
 * Created by shubham.agarwal on 2/5/17.
 */

public class BaseResponse extends BaseObservable implements Parcelable {

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel in) {
            return new BaseResponse(in);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };
    @SuppressWarnings("unused")
    private static final String TAG = "BaseResponse";
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("responseCode")
    @Expose
    private int responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("downloadMessage")
    @Expose
    private String downloadMessage;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("itemsPerPage")
    @Expose
    private int itemsPerPage;
    @SerializedName("cartCount")
    @Expose
    private int cartCount;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("pricelist")
    @Expose
    private int pricelist;

    @SerializedName("is_email_verified")
    @Expose
    private boolean isEmailVerified;

    @SerializedName("downloadRequest")
    @Expose
    private boolean downloadRequest;

    @SerializedName("downloadUrl")
    @Expose
    private String downloadUrl;

    @SerializedName("addons")
    @Expose
    private AddOns addons;

    @SerializedName("is_approved")
    @Expose
    private boolean user_approved;

    @SerializedName("accessDenied")
    @Expose
    private boolean accessDenied;

    @SerializedName("isOnboarded")
    @Expose
    private boolean isOnboarded;


    protected BaseResponse(Parcel in) {
        if (in == null) {
            return;
        }
        success = in.readByte() != 0;
        isEmailVerified = in.readByte() != 0;
        responseCode = in.readInt();
        message = in.readString();
        lang = in.readString();
        itemsPerPage = in.readInt();
        cartCount = in.readInt();
        customerId = in.readString();
        userId = in.readString();
        pricelist = in.readInt();
        addons = in.readParcelable(AddOns.class.getClassLoader());
        accessDenied = in.readByte() !=0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeByte((byte) (isEmailVerified ? 1 : 0));
        dest.writeInt(responseCode);
        dest.writeString(message);
        dest.writeString(lang);
        dest.writeInt(itemsPerPage);
        dest.writeInt(cartCount);
        dest.writeString(customerId);
        dest.writeString(userId);
        dest.writeInt(pricelist);
        dest.writeParcelable(addons, flags);
        dest.writeByte((byte) (accessDenied ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSuccess() {
        return success;
    }

    @SuppressWarnings("unused")
    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        if (message == null) {
            return "";
        }
        return message;
    }

    public String getLang() {
        if (lang == null) {
            return "";
        }
        return lang;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public boolean isUserApproved() {
        return user_approved;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public String getCustomerId() {
        if (customerId == null) {
            return "";
        }
        return customerId;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        if (userId == null) {
            return "";
        }
        return userId;
    }

    @SuppressWarnings("unused")
    public int getPricelist() {
        return pricelist;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowWishlistModule() {
        if (addons == null) {
            return false;
        }
        return addons.isWishlistModuleInstalled();
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isMarketplaceAllowed() {
        if (addons == null) {
            return false;
        }
        return addons.isOdooMarketplace();
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowReviewModule() {
        if (addons == null) {
            return false;
        }
        return addons.isReviewModuleInstalled();
    }

    public boolean isEmailVerified() {
        if (addons != null && addons.isEmailVerification()) {
            return isEmailVerified;
        } else {
            return true;
        }
    }

    public boolean isGdprEnable() {
        if (addons != null && addons.isOdooGdpr()) {
            return addons.isOdooGdpr();
        } else {
            return false;
        }
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public AddOns getAddons() {
        return addons;
    }

    public void setAddons(AddOns addons) {
        this.addons = addons;
    }

    public String getDownloadMessage() {
        return downloadMessage;
    }

    public boolean isDownloadRequest() {
        return downloadRequest;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public void setAccessDenied(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public boolean isUserOnboarded() {
        return isOnboarded;
    }
}
