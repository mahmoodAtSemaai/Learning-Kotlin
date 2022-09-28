package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class CartEntity(
    val productName: String,
    @SerializedName("cart_count")
    val cartCount: Int,
    @SerializedName("products")
    val products: List<CartProductItemEntity>,
    val cartId: Int
)
