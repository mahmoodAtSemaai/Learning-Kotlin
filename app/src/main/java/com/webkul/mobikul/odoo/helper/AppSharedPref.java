package com.webkul.mobikul.odoo.helper;

import static android.content.Context.MODE_PRIVATE;
import static com.webkul.mobikul.odoo.BuildConfig.DEFAULT_ITEM_PER_PAGE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shubham Agarwal on 4/1/17. @Webkul Software Private limited
 */

public class AppSharedPref {

    /* Shared Preferences keys */
    @SuppressWarnings("WeakerAccess")
    public static final String CUSTOMER_PREF = "CUSTOMER_PREF";
    @SuppressWarnings("WeakerAccess")
    public static final String SPLASH_PREF = "SPLASH_PREF";
    @SuppressWarnings("unused")
    private static final String TAG = "AppSharedPref";
//    @SuppressWarnings("unused")
//    private static final String CONFIGURATION_PREF = "CONFIGURATION_PREF";

    private static final String KEY_CUSTOMER_LOGIN_BASE_64_STR = "CUSTOMER_LOGIN_BASE_64_STR";
    private static final String KEY_CUSTOMER_LOGIN_BASE_64_STR_FOR_FINGERPRINT = "CUSTOMER_LOGIN_BASE_64_STR_FOR_FINGERPRINT";
    private static final String KEY_CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String KEY_CUSTOMER_ID = "CUSTOMER_ID";
    private static final String KEY_CUSTOMER_EMAIL = "CUSTOMER_EMAIL";
    private static final String KEY_CUSTOMER_PHONE_NUMBER = "CUSTOMER_PHONE_NUMBER";
    private static final String KEY_CUSTOMER_PROFILE_IMAGE = "CUSTOMER_PROFILE_IMAGE";
    private static final String KEY_CUSTOMER_BANNER_IMAGE = "CUSTOMER_BANNER_IMAGE";
    private static final String KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN = "CUSTOMER_IS_SOCIAL_LOGGED_IN";
    private static final String KEY_IS_CUSTOMER_REDEEM_POINTS = "IS_CUSTOMER_REDEEM_POINTS";
    private static final String KEY_CUSTOMER_JWT_AUTH_TOKEN = "CUSTOMER_JWT_AUTH_TOKEN";
    private static final String KEY_REFERRAL_CODE = "REFERRAL_CODE";
    private static final String KEY_ORDER_ID = "ORDER_ID";

    private static final String KEY_CART_COUNT = "CART_COUNT";
    private static final String DARK_MODE = "DARK_MODE";
    private static final String KEY_ITEMS_PER_PAGE = "ITEMS_PER_PAGE";

    /*SPLASH PREF*/
    private static final String KEY_IS_ALLOWED_RESET_PASSWORD = "ALLOW_RESET_PASSWORD";
    private static final String KEY_IS_ALLOW_GUEST_CHECKOUT = "ALLOW_GUEST_CHECKOUT";
    private static final String KEY_IS_ALLOW_WISHLIST_MODULE = "ALLOW_WISHLIST_MODULE";
    private static final String KEY_IS_ALLOW_REVIEW_MODULE = "ALLOW_REVIEW_MODULE";
    private static final String KEY_IS_ALLOW_SIGN_UP = "ALLOW_SIGN_UP";
    private static final String KEY_IS_EMAIL_VERIFIED = "IS_EMAIL_VERIFIED";
    private static final String KEY_IS_MARKETPLACE_ALLOWED = "IS_MARKETPLACE_ALLOWED";

    private static final String KEY_IS_ALLOW_GMAIL_SIGN_IN = "ALLOW_GMAIL_SIGN_IN";
    private static final String KEY_IS_ALLOW_FACEBOOK_SIGN_IN = "ALLOW_FACEBOOK_SIGN_IN";
    private static final String KEY_IS_ALLOW_TWITTER_SIGN_IN = "ALLOW_TWITTER_SIGN_IN";


    private static final String KEY_LAUNCH_COUNT = "LAUNCH_COUNT";
    private static final String KEY_DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH";
    private static final String KEY_IS_SELLER = "IS_SELLER";
    private static final String KEY_ALLOW_SHIPPING = "allow_shipping";
    private static final String KEY_LANGUAGE_CODE = "language_code";

    private static final String KEY_IS_ALLOWED_FINGERPRINT_LOGIN = "ALLOW_FINGERPRINT_LOGIN";
    private static final String KEY_TERM_AND_CONDITION = "TERM_AND_CONDITION";
    private static final String KEY_ODOO_GDPR = "ODOO_GDPR";
    private static final String KEY_IS_GRID = "Is_GRID";
    private static final String KEY_IS_APP_RUN_FIRST_TIME = "KEY_IS_APP_RUN_FIRST_TIME";
    private static final String KEY_IS_LANGUAGE_CHANGE = "KEY_IS_LANGUAGE_CHANGE";
    private static final String KEY_IS_DARK_CHANGE = "KEY_IS_DARK_CHANGE";
    private static final String KEY_IS_RECENT_CLEAR = "KEY_IS_RECENT_CLEAR";
    private static final String KEY_IS_RECENT_ENABLE = "KEY_IS_RECENT_ENABLE";
    private static final String KEY_IS_SEARCH_ENABLE = "KEY_IS_SEARCH_ENABLE";
    private static final String PRIVACY_POLICY_URL = "PRIVACY_POLICY_URL";
    private static final String ANALYTICS_ID = "ANALYTICS_ID";
    private static final String KEY_IS_USER_APPROVED = "KEY_IS_USER_APPROVED";


    /*SHARED PREF AND EDITOR*/
    @SuppressWarnings("WeakerAccess")
    public static SharedPreferences getSharedPreference(Context context, String preferenceFile) {
        return context.getSharedPreferences(preferenceFile, MODE_PRIVATE);
    }

    @SuppressWarnings("WeakerAccess")
    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context, String preferenceFile) {
        return context.getSharedPreferences(preferenceFile, MODE_PRIVATE).edit();
    }

    /*IS LOGGED IN*/
    public static boolean isDarkMode(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(DARK_MODE, false);
    }

    /*IS SOCIAL LOGIN*/
    public static void setDarkMode(Context context, boolean isDarkMode) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(DARK_MODE, isDarkMode).apply();
    }

    /*Customer*/

    /*IS LOGGED IN*/
    public static boolean isLoggedIn(Context context) {
        return !getCustomerLoginBase64Str(context).isEmpty() || !getAuthToken(context).isEmpty();
    }

    /*IS SOCIAL LOGIN*/
    public static boolean isSocialLoggedIn(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getBoolean(KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN, false);
    }

    public static void setSocialLoggedIn(Context context, boolean isSocialLoggedIn) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putBoolean(KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN, isSocialLoggedIn).apply();
    }

    /*CUSTOMER NAME*/
    public static String getCustomerName(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_NAME, "");
    }

    public static void setCustomerName(Context context, String customerName) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_NAME, customerName).apply();
    }

    /*CUSTOMER LOGIN BASE 64 STRING*/
    public static String getCustomerLoginBase64Str(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_LOGIN_BASE_64_STR, "").trim();
    }

    public static void setCustomerLoginBase64Str(Context context, String customerLoginBase64Str) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_LOGIN_BASE_64_STR, customerLoginBase64Str.trim()).apply();
    }

    /* CUSTOMER APPROVED */
    public static boolean getUserIsApproved(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getBoolean(KEY_IS_USER_APPROVED, true);
    }

    public static void setUserIsApproved(Context context, Boolean approved) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putBoolean(KEY_IS_USER_APPROVED, approved).apply();
    }


    /*Customer Email*/
    public static String getCustomerEmail(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_EMAIL, "");
    }

    public static void setCustomerEmail(Context context, String email) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_EMAIL, email).apply();
    }

    /*Customer Phone Number*/
    public static String getCustomerPhoneNumber(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_PHONE_NUMBER, "");
    }

    public static void setCustomerPhoneNumber(Context context, String phoneNumber) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_PHONE_NUMBER, phoneNumber).apply();
    }

    /*Customer Id*/
    public static String getCustomerId(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_ID, "");
    }

    public static void setCustomerId(Context context, String customerId) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_ID, customerId).apply();
    }

    /*CUSTOMER PROFILE IMAGE*/
    public static String getCustomerProfileImage(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_PROFILE_IMAGE, "");
    }

    public static void setCustomerProfileImage(Context context, String customerProfileImage) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_PROFILE_IMAGE, customerProfileImage).apply();
    }

    /*IS CUSTOMER REDEEM POINTS*/
    public static Boolean getIsCustomerWantToRedeemPoints(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getBoolean(KEY_IS_CUSTOMER_REDEEM_POINTS, false);
    }

    public static void setIsCustomerWantToRedeemPoints(Context context, Boolean isCustomerRedeemPoints) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putBoolean(KEY_IS_CUSTOMER_REDEEM_POINTS, isCustomerRedeemPoints).apply();
    }

    /*Referral Code*/
    public static String getReferralCode(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_REFERRAL_CODE, "");
    }

    public static void setReferralCode(Context context, String referralCode) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_REFERRAL_CODE, referralCode).apply();
    }

    /*Order Id*/
    public static int getOrderId(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getInt(KEY_ORDER_ID, 0);
    }

    public static void setOrderId(Context context, int orderId) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putInt(KEY_ORDER_ID, orderId).apply();
    }

    /* Is Customer a Seller too */
    public static boolean isSeller(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getBoolean(KEY_IS_SELLER, false);
    }

    public static void setIsSeller(Context context, boolean seller) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putBoolean(KEY_IS_SELLER, seller).apply();
    }


    /*CUSTOMER BANNER IMAGE*/

    public static String getCustomerBannerImage(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_BANNER_IMAGE, "");
    }

    public static void setCustomerBannerImage(Context context, String customerBannerImage) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putString(KEY_CUSTOMER_BANNER_IMAGE, customerBannerImage).apply();
    }

    /*CART COUNT -- OF A CUSTOMER*/

    public static int getCartCount(Context context, int defaultValue) {
        return getSharedPreference(context, CUSTOMER_PREF).getInt(KEY_CART_COUNT, defaultValue);
    }


    public static void setCartCount(Context context, int cartCount) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putInt(KEY_CART_COUNT, cartCount).apply();
    }

    /*ITEMS PER PAGE*/
    public static int getItemsPerPage(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getInt(KEY_ITEMS_PER_PAGE, DEFAULT_ITEM_PER_PAGE);
    }


    public static void setItemsPerPage(Context context, int itemPerPage) {
        getSharedPreferenceEditor(context, CUSTOMER_PREF).putInt(KEY_ITEMS_PER_PAGE, itemPerPage).apply();
    }

    /*CLEAR CUSTOMER DATA*/
    public static void clearCustomerData(Context context) {
        AppSharedPref.getSharedPreferenceEditor(context, CUSTOMER_PREF).clear().apply();
    }

    public static void clearUserAnalytics(Context context) {
        AppSharedPref.getSharedPreferenceEditor(context, SPLASH_PREF).clear().apply();
    }




    /*SPLASH PERSISITANCE DATA*/

    /*ALLOW RESET PASSWORD*/
    public static boolean isAllowResetPassword(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOWED_RESET_PASSWORD, false);
    }

    public static void setAllowResetPassword(Context context, boolean allowResetPwd) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOWED_RESET_PASSWORD, allowResetPwd).apply();
    }


    /*ALLOW GUEST CHECKOUT */
    public static boolean isAllowedGuestCheckout(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_GUEST_CHECKOUT, false);
    }

    public static void setAllowedGuestCheckout(Context context, boolean allowGuestCheckout) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_GUEST_CHECKOUT, allowGuestCheckout).apply();
    }

    /*ALLOW WISHLIST */
    public static boolean isAllowedWishlist(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_WISHLIST_MODULE, false);
    }

    public static void setAllowedWishlist(Context context, boolean allowWishlistModule) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_WISHLIST_MODULE, allowWishlistModule).apply();
    }

    public static boolean isAllowedReview(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_REVIEW_MODULE, false);
    }

    public static void setAllowedReview(Context context, boolean allowReviewModule) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_REVIEW_MODULE, allowReviewModule).apply();
    }

    public static boolean isEmailVerified(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_EMAIL_VERIFIED, false);
    }

    public static void setEmailVerified(Context context, boolean emailVerified) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_EMAIL_VERIFIED, emailVerified).apply();
    }

    public static boolean isMarketplaceAllowed(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_MARKETPLACE_ALLOWED, false);
    }

    public static void setMarketplaceAllowed(Context context, boolean marketplaceAllowed) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_MARKETPLACE_ALLOWED, marketplaceAllowed).apply();
    }


    /*ALLOW SIGN UP*/
    public static boolean isAllowedSignup(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_SIGN_UP, false);
    }

    public static void setAllowedSignup(Context context, boolean allowSignUp) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_SIGN_UP, allowSignUp).apply();
    }


    public static boolean isSocialLoginAvailable(Context context) {
        return isAllowedFbSignIn(context) || isAllowedGmailSignIn(context) || isAllowedTwitterSignIn(context);
    }


    /*ALLOW GMAIL SIGN IN*/
    public static boolean isAllowedGmailSignIn(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_GMAIL_SIGN_IN, false);
    }

    public static void setAllowedGmailSignIn(Context context, boolean allowGmailSignIn) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_GMAIL_SIGN_IN, allowGmailSignIn).apply();
    }


    /*ALLOW FACEBOOK SIGN IN*/
    public static boolean isAllowedFbSignIn(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_FACEBOOK_SIGN_IN, false);
    }

    public static void setAllowedFbSignIn(Context context, boolean allowFbSignIn) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_FACEBOOK_SIGN_IN, allowFbSignIn).apply();
    }


    /*ALLOW TWITTER SIGN IN*/
    public static boolean isAllowedTwitterSignIn(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOW_TWITTER_SIGN_IN, false);
    }

    public static void setAllowedTwitterSignIn(Context context, boolean allowTwitterSignIn) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOW_TWITTER_SIGN_IN, allowTwitterSignIn).apply();
    }


    /*LAUNCH COUNT*/
    public static long getLaunchCount(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getLong(KEY_LAUNCH_COUNT, 0);
    }

    public static void setLaunchcount(Context context, long lauchCount) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putLong(KEY_LAUNCH_COUNT, lauchCount).apply();
    }

    /*DATE_FIRST_LAUNCH*/

    public static long getFirstLaunchDate(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getLong(KEY_DATE_FIRST_LAUNCH, 0);
    }

    public static void setFirstLaunchDate(Context context, long firstLaunchDate) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putLong(KEY_DATE_FIRST_LAUNCH, firstLaunchDate).apply();
    }

    public static boolean isAllowShipping(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_ALLOW_SHIPPING, false);
    }

    public static void setAllowShipping(Context context, boolean allowShipping) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_ALLOW_SHIPPING, allowShipping).apply();
    }

    public static String getLanguageCode(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getString(KEY_LANGUAGE_CODE, "");
    }

    public static void setLanguageCode(Context context, String code) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putString(KEY_LANGUAGE_CODE, code).apply();
    }

    public static boolean getIsAllowedFingerprintLogin(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_ALLOWED_FINGERPRINT_LOGIN, false);
    }

    public static void setIsAllowedFingerprintLogin(Context context, boolean permission) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_ALLOWED_FINGERPRINT_LOGIN, permission).apply();
    }

    /*CUSTOMER LOGIN BASE 64 STRING FOR FINGERPRINT LOGIN*/
    public static String getCustomerLoginBase64StrForFingerprint(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getString(KEY_CUSTOMER_LOGIN_BASE_64_STR_FOR_FINGERPRINT, "").trim();
    }

    public static void setCustomerLoginBase64StrForFingerprint(Context context, String customerLoginBase64Str) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putString(KEY_CUSTOMER_LOGIN_BASE_64_STR_FOR_FINGERPRINT, customerLoginBase64Str.trim()).apply();
    }

    public static boolean isTermAndCondition(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_TERM_AND_CONDITION, false);
    }

    public static void setIsTermAndCondition(Context context, boolean permission) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_TERM_AND_CONDITION, permission).apply();
    }

    public static boolean isGdprEnable(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_ODOO_GDPR, false);
    }

    public static void setGdprEnable(Context context, boolean allowReviewModule) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_ODOO_GDPR, allowReviewModule).apply();
    }

    public static boolean isGridview(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_GRID, false);
    }

    public static void setGridview(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_GRID, value).apply();
    }

    public static boolean isAppRunFirstTime(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_APP_RUN_FIRST_TIME, true);
    }

    public static void setIsAppRunFirstTime(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_APP_RUN_FIRST_TIME, value).apply();
    }

    public static boolean isLanguageChange(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_LANGUAGE_CHANGE, false);
    }

    public static boolean isDarkChange(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_DARK_CHANGE, false);
    }

    public static void setIsLanguageChange(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_LANGUAGE_CHANGE, value).apply();
    }

    public static void setIsDarkChange(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_DARK_CHANGE, value).apply();
    }

    //Setting Page
    public static boolean isRecentViewClear(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_RECENT_CLEAR, false);
    }


    public static void setRecentViewClear(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_RECENT_CLEAR, value).apply();
    }

    public static boolean isRecentViewEnable(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_RECENT_ENABLE, true);
    }


    public static void setRecentViewEnable(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_RECENT_ENABLE, value).apply();
    }

    public static boolean isSearchEnable(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getBoolean(KEY_IS_SEARCH_ENABLE, false);
    }


    public static void setSearchEnable(Context context, boolean value) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putBoolean(KEY_IS_SEARCH_ENABLE, value).apply();
    }

    public static void setPrivacyPolicyURL(Context context, String privacy_policy_url) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putString(PRIVACY_POLICY_URL, privacy_policy_url).apply();
    }

    public static String getPrivacyURL(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getString(PRIVACY_POLICY_URL, null);
    }

    public static void setUserAnalyticsId(Context context, String analyticsId) {
        getSharedPreferenceEditor(context, SPLASH_PREF).putString(ANALYTICS_ID, analyticsId).apply();
    }

    public static String getUserAnalyticsId(Context context) {
        return getSharedPreference(context, SPLASH_PREF).getString(ANALYTICS_ID, null);
    }

    public static String getAuthToken(Context context) {
        return getSharedPreference(context, CUSTOMER_PREF).getString(KEY_CUSTOMER_JWT_AUTH_TOKEN, "");
    }
}
