package com.webkul.mobikul.odoo.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class UpdateCartRequest(
    @SerializedName("add_qty")
    @Expose
    var addQty: Int,

    @SerializedName("set_qty")
    @Expose
    var setQty: Int
) {


    fun getEditRequestBody(): String {
        return getRequestBody()
    }


    fun getRequestBody() =
        JSONObject().apply {
            put(ADD_QUANTITY, addQty)
            put(SET_QUANTITY, setQty)
        }.toString()

    companion object {
        private const val ADD_QUANTITY = "add_qty"
        private const val SET_QUANTITY = "set_qty"
    }
}
