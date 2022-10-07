package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class SubDistrictListEntity(
    val status: String?,
    @SerializedName("data")
    val subDistricts: ArrayList<SubDistrictEntity>
)
