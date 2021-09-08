package com.webkul.mobikul.odoo.model.customer.signup;

import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

/**
 * Created by shubham.agarwal on 9/5/17.
 */

public class SignUpResponse extends BaseResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "SignUpResponse";


    @SerializedName("homepage")
    @Expose
    private HomePageResponse homePageResponse;
    @SerializedName("login")
    @Expose
    private LoginResponse login;


    protected SignUpResponse(@Nullable Parcel in) {
        super(in);
    }

    public HomePageResponse getHomePageResponse() {
        if (homePageResponse == null) {
            return new HomePageResponse(null);
        }
        return homePageResponse;
    }

    public LoginResponse getLogin() {
        return login;
    }

//    public void updateSharedPref(Context context, String password) {
//        if (!password.isEmpty()) {
//            AppSharedPref.setCustomerLoginBase64Str(context, Base64.encodeToString(new AuthenticationRequest(getCustomerEmail(), password).toString().getBytes(), Base64.NO_WRAP));
//        }
//        AppSharedPref.setCustomerEmail(context, getCustomerEmail());
//        AppSharedPref.setCustomerName(context, getCustomerName());
//        AppSharedPref.setCustomerProfileImage(context, getCustomerProfileImage());
//        AppSharedPref.setCustomerBannerImage(context, getCustomerBannerImage());
//        AppSharedPref.setCustomerId(context, getCustomerId());
//    }
}
