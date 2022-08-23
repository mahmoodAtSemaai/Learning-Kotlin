package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class SignUpV1Entity(
    @SerializedName("auth") val auth: String = "",
    @SerializedName("customerId") val customerId: String ="",
    @SerializedName("userId") val userId: String = ""
)