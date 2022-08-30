package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class CartProductsResponse(
    @SerializedName("cart_count")
    val cartCount: Int,

    @SerializedName("products")
    val products: List<CartProductItemResponse>
)