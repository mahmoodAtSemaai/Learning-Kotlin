package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status_code") val statusCode: String,
    @SerializedName("status") val status: Any,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: T
)
