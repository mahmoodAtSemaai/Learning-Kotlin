package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class OtpAuthenticationResponse(
    @SerializedName("auth") val auth: String,
    @SerializedName("customerId") val customerId: String = "",
    @SerializedName("userId") val userId: String = ""
)
