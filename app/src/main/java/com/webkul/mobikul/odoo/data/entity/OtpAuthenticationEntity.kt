package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class OtpAuthenticationEntity(
    @SerializedName("auth") val auth: String,
    @SerializedName("customerId") val customerId: String = ""
)