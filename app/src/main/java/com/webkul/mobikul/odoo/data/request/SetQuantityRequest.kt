package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class SetQuantityRequest(
    @SerializedName("add_qty") val addQty : Int,
    @SerializedName("set_qty") val setQty : Int
) {

    fun getRequestBody() =
        JSONObject().apply {
            put(ADD_QUANTITY, addQty)
            put(SET_QUANTITY, setQty)
        }.toString()

    companion object{
        private const val ADD_QUANTITY = "add_qty"
        private const val SET_QUANTITY = "set_qty"
    }
}