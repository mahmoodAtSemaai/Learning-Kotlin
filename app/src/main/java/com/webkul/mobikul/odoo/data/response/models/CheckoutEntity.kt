package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutEntity(
    @SerializedName("partner_id")
    @Expose
    val partnerId: Int,
    @SerializedName("user_id")
    @Expose
    val user_id: Int,
    @SerializedName("product_count")
    @Expose
    val cartCount: Int,
    @SerializedName("pointsRedeemed")
    @Expose
    val pointsRedeemed: Boolean,
    @SerializedName("grand_total")
    @Expose
    val grandTotal: Int
)