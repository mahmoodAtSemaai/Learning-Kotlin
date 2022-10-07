package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class CustomerGroupResponse(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("order") val order : Int
)
