package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

data class SubDistrictEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_available") val isAvailable: Boolean
)