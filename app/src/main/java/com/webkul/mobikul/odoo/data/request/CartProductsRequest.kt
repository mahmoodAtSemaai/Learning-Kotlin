package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

data class CartProductsRequest(
        @SerializedName("products")
        @Expose
        var products: ArrayList<CartProductItemRequest>
) {
    fun getRequestBody() =
            JSONObject().apply {

                val jsonArray = JSONArray()
                products.forEach {
                    jsonArray.put(it.getRequestBody())
                }

                //stringify product list
                put(PRODUCTS, jsonArray)
            }.toString()

    companion object {
        private const val PRODUCTS = "products"
    }
}
