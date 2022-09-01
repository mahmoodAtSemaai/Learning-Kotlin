package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class CartBaseResponse <T>(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: T
)
