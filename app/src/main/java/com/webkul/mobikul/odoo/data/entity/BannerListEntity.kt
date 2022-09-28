package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class BannerListEntity(
        @SerializedName("banners") val banners: List<BannerEntity>
)