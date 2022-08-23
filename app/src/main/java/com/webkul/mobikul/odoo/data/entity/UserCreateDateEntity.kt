package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class UserCreateDateEntity(
    @SerializedName("password_enabled") val passwordEnabled : Boolean
)
