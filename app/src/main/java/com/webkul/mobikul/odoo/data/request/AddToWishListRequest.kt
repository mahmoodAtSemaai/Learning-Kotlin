package com.webkul.mobikul.odoo.data.request

import org.json.JSONObject

data class AddToWishListRequest(
    val productId: Int
) {

    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("product_id", productId)
        return jsonObject.toString()
    }
}