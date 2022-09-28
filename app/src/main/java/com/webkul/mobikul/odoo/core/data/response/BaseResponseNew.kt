package com.webkul.mobikul.odoo.core.data.response

import com.google.gson.annotations.SerializedName

data class BaseResponseNew<T>(
        @SerializedName("status") val status: Boolean,
        @SerializedName("status_code") val statusCode: Int,
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: T?
)