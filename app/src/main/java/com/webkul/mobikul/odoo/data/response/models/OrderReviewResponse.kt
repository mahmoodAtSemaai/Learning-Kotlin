package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class OrderReviewResponse(
    @SerializedName("transaction_id") val transactionId: Int,
    @SerializedName("sale_order_id") val saleOrderId: Int,
    @SerializedName("updated_items") val updatedItems: ArrayList<String>,
    @SerializedName("cart_count") val cartCount: Int
)