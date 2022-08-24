package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

data class DistrictListEntity(
    @SerializedName("data") val districtList : List<DistrictEntity>
)
