package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class BannerListResponse(
        @SerializedName("banners") val banners: List<BannerResponse>
)