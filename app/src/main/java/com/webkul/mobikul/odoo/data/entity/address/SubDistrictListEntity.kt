package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

data class SubDistrictListEntity(
    @SerializedName("data") val subDistrictList: List<SubDistrictEntity>
)