package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishListUpdatedEntity(
    @SerializedName("partner_id")
    @Expose
    val partner_id: Int,
    @SerializedName("user_id")
    @Expose
    val user_id: Int,
    @SerializedName("cartCount")
    @Expose
    val cartCount: Int,
    @SerializedName("wishlisted")
    @Expose
    val wishListed: Boolean,
    @SerializedName("is_seller")
    @Expose
    val isSeller: Boolean,
    @SerializedName("product_name")
    @Expose
    val productName: String
)