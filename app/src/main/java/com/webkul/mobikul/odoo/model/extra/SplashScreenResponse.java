package com.webkul.mobikul.odoo.model.extra;

import android.content.Context;
import android.os.Parcel;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.connection.ApplicationSingleton;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham.agarwal on 10/5/17.
 */

public class SplashScreenResponse extends LoginResponse {
    @SuppressWarnings("unused")
    private static final String TAG = "SplashScreenResponse";

    /*DEFAULT DATA*/
    @SerializedName("allow_resetPwd")
    @Expose
    private boolean allowResetPwd;

    @SerializedName("allow_guestCheckout")
    @Expose
    private boolean allowGuestCheckout;

    @SerializedName("allow_module_website_wishlist")
    @Expose
    private boolean allowModuleWebsiteWishlist;

    @SerializedName("allow_signup")
    @Expose
    private boolean allowSignup;

    @SerializedName("allow_gmailSign")
    @Expose
    private boolean allow_gmailSign;

    @SerializedName("allow_facebookSign")
    @Expose
    private boolean allow_facebookSign;

    @SerializedName("allow_twitterSign")
    @Expose
    private boolean allow_twitterSign;

    @SerializedName("TermsAndConditions")
    @Expose
    private boolean TermsAndConditions;


    @SerializedName("privacy_policy_url")
    @Expose
    private String privacy_policy_url;


    /*SPLASH DATA*/
    @SerializedName("sortData")
    @Expose
    private List<List<String>> sortData = null;

    @SerializedName("RatingStatus")
    @Expose
    private List<List<String>> ratingStatus = null;

    @SerializedName("allowShipping")
    @Expose
    private boolean allowShipping;

//    @SerializedName("allLanguages")
//    @Expose
//    private List<List<String>> allLanguages = null;


    public String isPrivacy_policy_url() {
        return privacy_policy_url;
    }

    @SerializedName("defaultLanguage")
    @Expose
    private List<String> defaultLanguage = null;

    protected SplashScreenResponse(@Nullable Parcel in) {
        super(in);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowResetPwd() {
        return allowResetPwd;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowGuestCheckout() {
        return allowGuestCheckout;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowSignup() {
        return allowSignup;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllow_gmailSign() {
        return allow_gmailSign;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllow_facebookSign() {
        return allow_facebookSign;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllow_twitterSign() {
        return allow_twitterSign;
    }

    @SuppressWarnings("WeakerAccess")
    public List<List<String>> getSortData() {
        if (sortData == null) {
            return new ArrayList<>();
        }
        return sortData;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isAllowShipping() {
        return allowShipping;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isTermsAndConditions() {
        return TermsAndConditions;
    }

    @SuppressWarnings("WeakerAccess")
    public List<List<String>> getRatingStatus() {
        if (ratingStatus == null) {
            return new ArrayList<>();
        }
        return ratingStatus;
    }

    @Override
    @SuppressWarnings("WeakerAccess")
    public boolean isAllowWishlistModule() {
        return super.isAllowWishlistModule() && allowModuleWebsiteWishlist;
    }

//    public List<List<String>> getAllLanguages() {
//        return allLanguages;
//    }
//
//    public HashMap<String, String> getAllLanguagesHashMap() {
//        if(getAllLanguages() == null) {
//            return null;
//        }
//        HashMap<String, String> languageMap = new HashMap<>();
//        for(List<String> list : getAllLanguages()) {
//            languageMap.put(list.get(0), list.get(1));
//        }
//        return languageMap;
//    }

    public List<String> getDefaultLanguage() {
        if (defaultLanguage == null) {
            return new ArrayList<>();
        }
        return defaultLanguage;
    }

    @Override
    @SuppressWarnings("WeakerAccess")
    public boolean isMarketplaceAllowed() {
        return super.isMarketplaceAllowed();
    }

    public void updateSharedPref(Context context) {
        super.updateSharedPref(context, "");
        Log.d(TAG, "updateSharedPref: " + isAllowSignup());
        AppSharedPref.setAllowResetPassword(context, isAllowResetPwd());
        AppSharedPref.setAllowedGuestCheckout(context, isAllowGuestCheckout());
        AppSharedPref.setAllowedSignup(context, isAllowSignup());
        AppSharedPref.setAllowedWishlist(context, isAllowWishlistModule());
        AppSharedPref.setAllowedReview(context, isAllowReviewModule());
        AppSharedPref.setEmailVerified(context, isEmailVerified());
        AppSharedPref.setMarketplaceAllowed(context, isMarketplaceAllowed());
        AppSharedPref.setAllowShipping(context, isAllowShipping());
        if (AppSharedPref.getLanguageCode(context).isEmpty() && getDefaultLanguage().size() > 0) {
            AppSharedPref.setLanguageCode(context, getDefaultLanguage().get(0));
            BaseActivity.setLocale(context, true);
        }
        AppSharedPref.setIsLanguageChange(context, false);
        AppSharedPref.setAllowedGmailSignIn(context, isAllow_gmailSign());
        AppSharedPref.setAllowedFbSignIn(context, isAllow_facebookSign());
        AppSharedPref.setIsTermAndCondition(context, isTermsAndConditions());
        AppSharedPref.setAllowedTwitterSignIn(context, isAllow_twitterSign());
        AppSharedPref.setPrivacyPolicyURL(context, privacy_policy_url);

        ApplicationSingleton.getInstance().setSortData(getSortData());
        ApplicationSingleton.getInstance().setRatingStatus(getRatingStatus());

    }
}

