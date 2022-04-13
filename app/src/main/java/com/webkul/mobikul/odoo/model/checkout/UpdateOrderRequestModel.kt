package com.webkul.mobikul.odoo.model.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class UpdateOrderRequestModel(
    @SerializedName("carrier_id")
    @Expose
    val carrier_id: Int?,
    @SerializedName("partner_shipping_id")
    @Expose
    val partner_shipping_id: Int?
)