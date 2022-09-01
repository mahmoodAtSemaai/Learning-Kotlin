package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishListUpdatedResponse(
    @JvmField
    @SerializedName("message")
    @Expose
    val message: String,

    @JvmField
    @SerializedName("result")
    @Expose
    val result: Result,

    @JvmField
    @SerializedName("status")
    @Expose
    val status: String,

    @JvmField
    @SerializedName("status_code")
    @Expose
    val statusCode: Int,


)