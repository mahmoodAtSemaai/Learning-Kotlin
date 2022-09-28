package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class BannerEntity(
    @SerializedName("bannerName") val bannerName: String,
    @SerializedName("bannerType") val bannerType: String,
    @SerializedName("id") val id: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("url") val url: String,
    @SerializedName("domain") val domain: String,
    @SerializedName("loyalty_banner_id") val loyaltyTermId: String
)
