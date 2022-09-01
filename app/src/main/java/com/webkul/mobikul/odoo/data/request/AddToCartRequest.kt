package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class AddToCartRequest(

    @SerializedName("product_id")
    @Expose
    val productId: String,

    @SerializedName("add_qty")
    @Expose
    val addQty: Int,

    @SerializedName("set_qty")
    @Expose
    val setQty: Int = 0
) {

    fun getAddRequest(): String {
        return getRequestBody()
    }

    fun getRequestBody() =
        JSONObject().apply {
            put(PRODUCT_ID, productId)
            put(ADD_QUANTITY, addQty)
            put(SET_QUANTITY, setQty)
        }.toString()

    companion object{
        private const val PRODUCT_ID = "product_id"
        private const val ADD_QUANTITY = "add_qty"
        private const val SET_QUANTITY = "set_qty"
    }
}