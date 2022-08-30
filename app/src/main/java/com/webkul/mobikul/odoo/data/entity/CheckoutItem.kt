package com.webkul.mobikul.odoo.data.entity

import org.json.JSONObject

data class CheckoutItem(
    val productId: Int,
    val quantity: Int
) {

    fun getRawObjectAsRawJsonString() = JSONObject().apply {
        put(PRODUCT_ID, productId)
        put(QUANTITY, quantity)
    }.toString()

    companion object{
        private const val PRODUCT_ID = "product_id"
        private const val QUANTITY = "qty"
    }
}