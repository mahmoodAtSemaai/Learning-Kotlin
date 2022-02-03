package com.webkul.mobikul.odoo.model.customer.signin;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;

/**
 * Created by shubham.agarwal on 17/5/17.
 */

public class LoginResponse extends BaseResponse implements Parcelable {
    @SuppressWarnings("unused")
    private static final String TAG = "LoginResponse";


    /*DETAILED*/
    @SerializedName("customerBannerImage")
    @Expose
    private String customerBannerImage;

    @SerializedName("customerProfileImage")
    @Expose
    private String customerProfileImage;

    @SerializedName("cartId")
    @Expose
    private String cartId;

    @SerializedName("themeCode")
    @Expose
    private String themeCode;

    @SerializedName("customerName")
    @Expose
    private String customerName;

    @SerializedName("customerEmail")
    @Expose
    private String customerEmail;

    @SerializedName("customerPhoneNumber")
    @Expose
    private String customerPhoneNumber;

    @SerializedName("is_approverd")
    @Expose
    private boolean user_approved;

    @SerializedName("customerLang")
    @Expose
    private String customerLang;

    @SerializedName("is_seller")
    @Expose
    private boolean isSeller;


//    protected LoginResponse(@Nullable Parcel in) {
//        super(in);
//    }

    protected LoginResponse(Parcel in) {
        super(in);
        customerBannerImage = in.readString();
        customerProfileImage = in.readString();
        cartId = in.readString();
        themeCode = in.readString();
        customerName = in.readString();
        customerEmail = in.readString();
        customerPhoneNumber = in.readString();
        customerLang = in.readString();
        isSeller = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(customerBannerImage);
        dest.writeString(customerProfileImage);
        dest.writeString(cartId);
        dest.writeString(themeCode);
        dest.writeString(customerName);
        dest.writeString(customerEmail);
        dest.writeString(customerPhoneNumber);
        dest.writeString(customerLang);
        dest.writeByte((byte) (isSeller ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    @SuppressWarnings("unused")
    public String getCustomerLang() {
        if (customerLang == null) {
            return "";
        }
        return customerLang;
    }

    public String getCustomerName() {
        if (customerName == null) {
            return "";
        }

        return customerName;
    }

    public String getCustomerEmail() {
        if (customerEmail == null) {
            return "";
        }

        return customerEmail;
    }

    public String getCustomerPhoneNumber() {
        if (customerPhoneNumber == null) {
            return "";
        }
        return customerPhoneNumber;
    }

    @SuppressWarnings("unused")
    public String getCartId() {
        if (cartId == null) {
            return "";
        }

        return cartId;
    }

    public String getCustomerProfileImage() {
        if (customerProfileImage == null) {
            return "";
        }

        return customerProfileImage;
    }

    @SuppressWarnings("unused")
    public String getThemeCode() {
        if (themeCode == null) {
            return "";
        }
        return themeCode;
    }

    @SuppressWarnings("WeakerAccess")
    public String getCustomerBannerImage() {
        if (customerBannerImage == null) {
            return "";
        }
        return customerBannerImage;
    }

    public boolean isUserApproved() {
        return user_approved;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void updateSharedPref(Context context, String password) {
        if (!password.isEmpty()) {
            AppSharedPref.setCustomerLoginBase64Str(context, Base64.encodeToString(new AuthenticationRequest(getCustomerPhoneNumber(), password).toString().getBytes(), Base64.NO_WRAP));
        }
        Log.d("TAG", "LoginResponse  updateSharedPref: " + getCustomerName());
        if (getCustomerName() != null && !getCustomerName().equals("")) {
            AppSharedPref.setCustomerName(context, getCustomerName());
        }
        if (getCustomerEmail() != null && !getCustomerEmail().equals("")) {
            AppSharedPref.setCustomerEmail(context, getCustomerEmail());
        }

        if (getCustomerPhoneNumber() != null && !getCustomerPhoneNumber().equals("")) {
            AppSharedPref.setCustomerPhoneNumber(context, getCustomerPhoneNumber());
        }

        if (getCustomerProfileImage() != null && !getCustomerProfileImage().equals("")) {
            AppSharedPref.setCustomerProfileImage(context, getCustomerProfileImage());
        }
        AppSharedPref.setCustomerBannerImage(context, getCustomerBannerImage());
        AppSharedPref.setCustomerId(context, getCustomerId());
        if (BuildConfig.isMarketplace) {
            AppSharedPref.setIsSeller(context, isSeller());
        }
    }
}
