package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class BaseOtpLoginResponse<T>(
    @SerializedName("status_code") val statusCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: T
)
