package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class CartProductItemRequest(
        @SerializedName("product_id")
        @Expose
        var productId: Int,

        @SerializedName("add_qty")
        @Expose
        var addQty: Int,

        @SerializedName("set_qty")
        @Expose
        var setQty: Int
) {
    fun getRequestBody() =
            JSONObject().apply {
                put(PRODUCT_ID, productId)
                put(ADD_QUANTITY, addQty)
                put(SET_QUANTITY, setQty)
            }

    companion object {
        private const val PRODUCT_ID = "product_id"
        private const val ADD_QUANTITY = "add_qty"
        private const val SET_QUANTITY = "set_qty"
    }
}