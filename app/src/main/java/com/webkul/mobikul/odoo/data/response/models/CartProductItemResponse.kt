package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class CartProductItemResponse(
    @SerializedName("line_id")
    val lineId: Int,

    @SerializedName("option_ids")
    val optionIds: List<Int>,

    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("warning")
    val warning: String
)