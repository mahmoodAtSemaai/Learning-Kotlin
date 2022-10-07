package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class DiscountOrderPriceEntity(
    @SerializedName("discounted_price")
    val discountPrice: String,
    @SerializedName("points_redeemed")
    val pointsRedeemed: Double
)
