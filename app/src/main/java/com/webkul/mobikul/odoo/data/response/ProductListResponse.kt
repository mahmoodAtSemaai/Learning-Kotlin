package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class ProductListResponse(
        @SerializedName("offset") val offset: Int,
        @SerializedName("tcount") val tCount: Int,
        @SerializedName("products") val products: List<ProductResponse>,
)
