package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class CartProductItemEntity(
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