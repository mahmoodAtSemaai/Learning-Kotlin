package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class CustomerGroupEntity(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("order") val order : Int
)
