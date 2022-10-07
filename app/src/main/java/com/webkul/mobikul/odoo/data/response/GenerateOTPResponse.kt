package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class GenerateOTPResponse(
    @SerializedName("status_code") val statusCode: String,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
)
