package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class AddToCartResponse(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("line_id") val lineId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("cart_count") val newCartCount: Int,
    @SerializedName("option_ids") val optionsId: ArrayList<Int>,
    @SerializedName("warning") val warning: String
)