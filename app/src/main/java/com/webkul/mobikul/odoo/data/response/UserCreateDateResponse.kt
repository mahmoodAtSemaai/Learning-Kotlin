package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class UserCreateDateResponse(
    @SerializedName("password_enabled") val passwordEnabled: Boolean
)