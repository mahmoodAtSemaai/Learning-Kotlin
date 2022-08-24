package com.webkul.mobikul.odoo.core.data.local

import android.content.Context
import android.content.SharedPreferences
import com.webkul.mobikul.odoo.core.utils.PRIVACY_POLICY_URL_DEFAULT
import javax.inject.Inject

class AppPreferences @Inject constructor(private val context: Context) {

    companion object {
        private const val CUSTOMER_PREF_NAME = "CUSTOMER_PREF"
        private const val SPLASH_PREF_NAME = "SPLASH_PREF"

        private const val MODE = Context.MODE_PRIVATE


        private const val KEY_CUSTOMER_LOGIN_BASE_64_STR = "CUSTOMER_LOGIN_BASE_64_STR"
        private const val KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN = "CUSTOMER_IS_SOCIAL_LOGGED_IN"
        private const val KEY_CUSTOMER_JWT_AUTH_TOKEN = "CUSTOMER_JWT_AUTH_TOKEN"
        private const val KEY_LANGUAGE_CODE =
                "language_code" //Keeping Consistent with previous preference value

        private const val ANALYTICS_ID = "ANALYTICS_ID"

        private const val PRIVACY_POLICY_URL = "PRIVACY_POLICY_URL"

        /*SPLASH PREF*/
        private const val KEY_IS_ALLOWED_RESET_PASSWORD = "ALLOW_RESET_PASSWORD"
        private const val KEY_IS_ALLOW_GUEST_CHECKOUT = "ALLOW_GUEST_CHECKOUT"
        private const val KEY_IS_ALLOW_WISHLIST_MODULE = "ALLOW_WISHLIST_MODULE"
        private const val KEY_IS_ALLOW_REVIEW_MODULE = "ALLOW_REVIEW_MODULE"
        private const val KEY_IS_ALLOW_SIGN_UP = "ALLOW_SIGN_UP"
        private const val KEY_IS_EMAIL_VERIFIED = "IS_EMAIL_VERIFIED"
        private const val KEY_IS_MARKETPLACE_ALLOWED = "IS_MARKETPLACE_ALLOWED"

        private const val KEY_IS_ALLOW_GMAIL_SIGN_IN = "ALLOW_GMAIL_SIGN_IN"
        private const val KEY_IS_ALLOW_FACEBOOK_SIGN_IN = "ALLOW_FACEBOOK_SIGN_IN"
        private const val KEY_IS_ALLOW_TWITTER_SIGN_IN = "ALLOW_TWITTER_SIGN_IN"


        private const val KEY_LAUNCH_COUNT = "LAUNCH_COUNT"
        private const val KEY_DATE_FIRST_LAUNCH = "DATE_FIRST_LAUNCH"
        private const val KEY_IS_SELLER = "IS_SELLER"
        private const val KEY_ALLOW_SHIPPING = "allow_shipping"
        private const val KEY_IS_USER_APPROVED = "KEY_IS_USER_APPROVED"

        private const val KEY_IS_ALLOWED_FINGERPRINT_LOGIN = "ALLOW_FINGERPRINT_LOGIN"
        private const val KEY_TERM_AND_CONDITION = "TERM_AND_CONDITION"
        private const val KEY_ODOO_GDPR = "ODOO_GDPR"
        private const val KEY_IS_GRID = "Is_GRID"
        private const val KEY_IS_APP_RUN_FIRST_TIME = "KEY_IS_APP_RUN_FIRST_TIME"
        private const val KEY_IS_LANGUAGE_CHANGE = "KEY_IS_LANGUAGE_CHANGE"
        private const val KEY_IS_DARK_CHANGE = "KEY_IS_DARK_CHANGE"
        private const val KEY_IS_RECENT_CLEAR = "KEY_IS_RECENT_CLEAR"
        private const val KEY_IS_RECENT_ENABLE = "KEY_IS_RECENT_ENABLE"
        private const val KEY_IS_SEARCH_ENABLE = "KEY_IS_SEARCH_ENABLE"

        private const val KEY_IS_FCM_TOKEN_SYNCED = "KEY_IS_FCM_TOKEN_SYNCED"

        /*Customer Details*/
        private const val KEY_CUSTOMER_NAME = "CUSTOMER_NAME"
        private const val KEY_CUSTOMER_ID = "CUSTOMER_ID"
        private const val KEY_CUSTOMER_EMAIL = "CUSTOMER_EMAIL"
        private const val KEY_CUSTOMER_PHONE_NUMBER = "CUSTOMER_PHONE_NUMBER"
        private const val KEY_CUSTOMER_PROFILE_IMAGE = "CUSTOMER_PROFILE_IMAGE"
        private const val KEY_CUSTOMER_BANNER_IMAGE = "CUSTOMER_BANNER_IMAGE"

        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_CUSTOMER_GROUP_NAME = "CUSTOMER_GROUP_NAME"
        private const val KEY_GROUP_NAME = "GROUP_NAME"
        private const val KEY_CUSTOMER_GROUP_ID = "CUSTOMER_GROUP_ID"
        private const val KEY_IS_USER_ONBOARDED = "KEY_IS_USER_ONBOARDED"
        private const val KEY_USER_NAME = "USER_NAME"

    }

    private val customerPreferences: SharedPreferences =
            context.getSharedPreferences(CUSTOMER_PREF_NAME, MODE)
    private val splashPreferences: SharedPreferences =
            context.getSharedPreferences(SPLASH_PREF_NAME, MODE)

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    val isLoggedIn: Boolean
        get() {
            val value = customerPreferences.getString(KEY_CUSTOMER_LOGIN_BASE_64_STR, "")
            return !value.isNullOrEmpty() || !authToken.isNullOrEmpty()
        }

    val isSocialLoggedIn: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_CUSTOMER_IS_SOCIAL_LOGGED_IN, false)
        }

    var languageCode: String?
        get() {
            return splashPreferences.getString(KEY_LANGUAGE_CODE, "id_ID")
        }
        set(value) = splashPreferences.edit {
            it.putString(KEY_LANGUAGE_CODE, value)
        }

    var customerLoginToken: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_LOGIN_BASE_64_STR, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_LOGIN_BASE_64_STR, value)
        }

    var analyticsId: String?
        get() {
            return customerPreferences.getString(ANALYTICS_ID, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(ANALYTICS_ID, value)
        }

    var privacyUrl: String?
        get() {
            return splashPreferences.getString(PRIVACY_POLICY_URL, "")
        }
        set(value) = splashPreferences.edit {
            it.putString(PRIVACY_POLICY_URL, value)
        }

    var authToken: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_JWT_AUTH_TOKEN, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_JWT_AUTH_TOKEN, value)
        }

    var customerId: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_ID, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_ID, value)
        }

    var userId: Int
        get() {
            return customerPreferences.getInt(KEY_USER_ID, -1)
        }
        set(value) = customerPreferences.edit {
            it.putInt(KEY_USER_ID, value)
        }

    var customerGroupName: String
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_GROUP_NAME, "").toString()
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_GROUP_NAME, value)
        }

    var customerGroupId: Int
        get() {
            return customerPreferences.getInt(KEY_CUSTOMER_GROUP_ID, -1)
        }
        set(value) = customerPreferences.edit {
            it.putInt(KEY_CUSTOMER_GROUP_ID, value)
        }

    var groupName: String
        get() {
            return customerPreferences.getString(KEY_GROUP_NAME, "").toString()
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_GROUP_NAME, value)
        }

    var userName: String
        get() {
            return customerPreferences.getString(KEY_USER_NAME, "").toString()
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_USER_NAME, value)
        }

    var isUserOnboarded: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_IS_USER_ONBOARDED, true)
        }
        set(value) = customerPreferences.edit {
            it.putBoolean(KEY_IS_USER_ONBOARDED, value)
        }

    var isFirstTime: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_APP_RUN_FIRST_TIME, true)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_APP_RUN_FIRST_TIME, value)
        }

    var isAllowResetPassword: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_ALLOWED_RESET_PASSWORD, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_ALLOWED_RESET_PASSWORD, value)
        }

    var isAllowedGuestCheckout: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_ALLOW_GUEST_CHECKOUT, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_ALLOW_GUEST_CHECKOUT, value)
        }

    var isAllowedWishlist: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_ALLOW_WISHLIST_MODULE, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_ALLOW_WISHLIST_MODULE, value)
        }

    var isAllowedReview: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_ALLOW_REVIEW_MODULE, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_ALLOW_REVIEW_MODULE, value)
        }

    var isEmailVerified: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_EMAIL_VERIFIED, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_EMAIL_VERIFIED, value)
        }

    var isMarketplaceAllowed: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_MARKETPLACE_ALLOWED, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_MARKETPLACE_ALLOWED, value)
        }

    var isAllowedSignup: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_ALLOW_SIGN_UP, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_ALLOW_SIGN_UP, value)
        }

    var isAllowShipping: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_ALLOW_SHIPPING, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_ALLOW_SHIPPING, value)
        }

    var launchCount: Long
        get() {
            return splashPreferences.getLong(KEY_LAUNCH_COUNT, 0)
        }
        set(value) = splashPreferences.edit {
            it.putLong(KEY_LAUNCH_COUNT, value)
        }

    var dateFirstLaunch: Long
        get() {
            return splashPreferences.getLong(KEY_DATE_FIRST_LAUNCH, 0)
        }
        set(value) = splashPreferences.edit {
            it.putLong(KEY_DATE_FIRST_LAUNCH, value)
        }

    var isLanguageChange: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_LANGUAGE_CHANGE, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_LANGUAGE_CHANGE, value)
        }

    var isTermAndCondition: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_TERM_AND_CONDITION, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_TERM_AND_CONDITION, value)
        }

    var isDarkChange: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_DARK_CHANGE, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_DARK_CHANGE, value)
        }

    var isRecentViewEnable: Boolean
        get() {
            return splashPreferences.getBoolean(KEY_IS_RECENT_ENABLE, false)
        }
        set(value) = splashPreferences.edit {
            it.putBoolean(KEY_IS_RECENT_ENABLE, value)
        }

    var privacyPolicyUrl: String?
        get() {
            return splashPreferences.getString(PRIVACY_POLICY_URL, PRIVACY_POLICY_URL_DEFAULT)
        }
        set(value) = splashPreferences.edit {
            it.putString(PRIVACY_POLICY_URL, value)
        }

    var customerName: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_NAME, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_NAME, value)
        }

    var customerEmail: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_EMAIL, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_EMAIL, value)
        }

    var customerPhoneNumber: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_PHONE_NUMBER, "")
        }
        set(value) = customerPreferences.edit {
            it.putString(KEY_CUSTOMER_PHONE_NUMBER, value)
        }

    var customerProfileImage: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_PROFILE_IMAGE, "")
        }
        set(value) = splashPreferences.edit {
            it.putString(KEY_CUSTOMER_PROFILE_IMAGE, value)
        }

    var customerBannerImage: String?
        get() {
            return customerPreferences.getString(KEY_CUSTOMER_BANNER_IMAGE, "")
        }
        set(value) = splashPreferences.edit {
            it.putString(KEY_CUSTOMER_BANNER_IMAGE, value)
        }

    var isSeller: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_IS_SELLER, false)
        }
        set(value) = customerPreferences.edit {
            it.putBoolean(KEY_IS_SELLER, value)
        }

    var isUserApproved: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_IS_USER_APPROVED, true)
        }
        set(value) = customerPreferences.edit {
            it.putBoolean(KEY_IS_USER_APPROVED, value)
        }

    var isFCMTokenSynced: Boolean
        get() {
            return customerPreferences.getBoolean(KEY_IS_FCM_TOKEN_SYNCED, false)
        }
        set(value) = customerPreferences.edit {
            it.putBoolean(KEY_IS_FCM_TOKEN_SYNCED, value)
        }

    fun clearCustomerData() {
        customerPreferences.edit {
            it.clear()
        }
    }
}