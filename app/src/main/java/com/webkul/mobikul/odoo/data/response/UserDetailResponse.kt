package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
        @SerializedName("addons") val addons: AddOnsResponse,
        @SerializedName("customer_id") val customerId: Int,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("cart_count") val cartCount: Int,
        @SerializedName("is_approved") val is_approved: Boolean,
        @SerializedName("wishlist_count") val wishlistCount: Int,
        @SerializedName("is_seller") val isSeller: Boolean,
        @SerializedName("default_language") val defaultLanguage: LanguageResponse,
        @SerializedName("all_languages") val allLanguages: List<LanguageResponse>,
        @SerializedName("terms_and_conditions") val termsAndConditions: Boolean,
        @SerializedName("wishlist") val wishlist : List<Int>
)
