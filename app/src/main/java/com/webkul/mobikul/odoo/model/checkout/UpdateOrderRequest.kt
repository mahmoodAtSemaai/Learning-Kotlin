package com.webkul.mobikul.odoo.model.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class UpdateOrderRequest(
    @SerializedName("vals")
    @Expose
    var updateOrderRequestModel: UpdateOrderRequestModel
) {
    override fun toString(): String {
        val valJsonObject = JSONObject()
        if(updateOrderRequestModel.carrier_id != null)
            valJsonObject.put("carrier_id", updateOrderRequestModel.carrier_id)
        if(updateOrderRequestModel.partner_shipping_id != null)
            valJsonObject.put("partner_shipping_id", updateOrderRequestModel.partner_shipping_id)

        val jsonObject = JSONObject()

        jsonObject.put("vals", valJsonObject)
        return jsonObject.toString()
    }
}