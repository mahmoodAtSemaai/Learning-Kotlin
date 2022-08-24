package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.extra.AddOns

data class SplashEntity(
        /*DEFAULT DATA*/
        @SerializedName("allow_resetPwd") var isAllowResetPwd: Boolean = false,
        @SerializedName("allow_guestCheckout") var isAllowGuestCheckout: Boolean = false,
        @SerializedName("allow_module_website_wishlist") var allowModuleWebsiteWishlist: Boolean = false,
        @SerializedName("allow_signup") var isAllowSignup: Boolean = false,
        @SerializedName("TermsAndConditions") var termsAndConditions: Boolean = false,
        @SerializedName("privacy_policy_url") var privacyPolicyUrl: String? = null,
        @SerializedName("sortData") val sortData: List<List<String>> = arrayListOf(),
        @SerializedName("RatingStatus") val ratingStatus: List<List<String>> = arrayListOf(),
        @SerializedName("allowShipping") var isAllowShipping: Boolean = false,
        @SerializedName("is_approved") var isUserApproved: Boolean = false,
        @SerializedName("isOnboarded") var isUserOnboarded: Boolean = false,
        @SerializedName("defaultLanguage") var defaultLanguage: List<String> = arrayListOf(),
        @SerializedName("customerName") var customerName: String? = null,
        @SerializedName("customerEmail") val customerEmail: String? = null,
        @SerializedName("customerPhoneNumber") val customerPhoneNumber: String? = null,
        @SerializedName("customerLang") val customerLang: String? = null,
        @SerializedName("is_seller") val isSeller: Boolean = false,
        @SerializedName("customerBannerImage") var customerBannerImage: String? = null,
        @SerializedName("customerProfileImage") var customerProfileImage: String? = null,
        @SerializedName("addons") var addons: AddOns? = null,
        @SerializedName("customerId") var customerId: String = "",
        @SerializedName("customerGroupId") var customerGroupId: Int? = null,
        @SerializedName("groupName") var groupName: String = "",
        @SerializedName("customerGroupName") var customerGroupName: String = "",
        @SerializedName("user_name") var userName: String = ""
) {
    fun isAllowWishlistModule(): Boolean {
        return allowModuleWebsiteWishlist && addons?.isWishlistModuleInstalled ?: false
    }

    fun isAllowReviewModule(): Boolean {
        return addons?.isReviewModuleInstalled ?: false
    }

    fun isEmailVerified(): Boolean {
        return addons?.isEmailVerification ?: false
    }

    fun isMarketplaceAllowed(): Boolean {
        return addons?.isOdooMarketplace ?: false
    }
}
