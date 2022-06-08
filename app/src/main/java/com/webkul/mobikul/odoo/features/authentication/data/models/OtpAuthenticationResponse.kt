package com.webkul.mobikul.odoo.features.authentication.data.models

import com.google.gson.annotations.SerializedName

data class OtpAuthenticationResponse(
    @SerializedName("auth") val auth: String,
    @SerializedName("customerId") val customerId: String = ""
)
