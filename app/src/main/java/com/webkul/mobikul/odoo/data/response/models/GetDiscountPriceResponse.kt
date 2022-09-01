package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class GetDiscountPriceResponse(
    @SerializedName("points_redeemed") val pointsRedeemed: Double,
    @SerializedName("discounted_price") val discountedPrice: String,
    @SerializedName("initial_price") val initialPrice: String,
    @SerializedName("semaai_points_balance") val semaaiPointsBalance: Double
)