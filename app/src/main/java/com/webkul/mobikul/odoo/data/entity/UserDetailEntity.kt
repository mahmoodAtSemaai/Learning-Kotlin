package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserDetailEntity(
        @SerializedName("addons") val addons: AddOnsEntity,
        @SerializedName("customer_id") val customerId: Int,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("cart_count") val cartCount: Int,
        @SerializedName("is_approved") val isApproved: Boolean,
        @SerializedName("wishlist_count") val wishlistCount: Int,
        @SerializedName("is_seller") val isSeller: Boolean,
        @SerializedName("default_language") val defaultLanguage: LanguageEntity,
        @SerializedName("all_languages") val allLanguages: List<LanguageEntity>,
        @SerializedName("terms_and_conditions") val termsAndConditions: Boolean,
        val newCartCount: Int,
        val cartId: Int
)
