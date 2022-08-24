package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class BaseUserOnboardingResponse<T>(
    @SerializedName("status_code") val statusCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: T
)