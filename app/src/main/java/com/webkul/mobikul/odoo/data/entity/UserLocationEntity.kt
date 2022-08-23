package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserLocationEntity(
    @SerializedName("user_id") val userId: String,
    @SerializedName("customerId") val customerId: String,
    @SerializedName("partner_latitude") val partnerLatitude: String,
    @SerializedName("partner_longitude") val partnerLongitude: String,
)
