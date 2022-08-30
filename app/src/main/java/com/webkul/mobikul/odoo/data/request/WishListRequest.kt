package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class WishListRequest(
    @SerializedName("product_id") val productId: Int
){
    override fun toString(): String {
        val jsonObject =  JSONObject()
        jsonObject.put("product_id", productId)
        return jsonObject.toString()
    }
}
