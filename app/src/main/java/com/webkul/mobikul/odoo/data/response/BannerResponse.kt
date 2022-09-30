package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("bannerName") val bannerName: String,
    @SerializedName("bannerType") val bannerType: String,
    @SerializedName("id") val bannerId: Int,
    @SerializedName("product_id") val productId: String,
    @SerializedName("url") val url: String,
    @SerializedName("domain") val domain: String,
    @SerializedName("loyalty_banner_id") val loyaltyTermId: Int
)
