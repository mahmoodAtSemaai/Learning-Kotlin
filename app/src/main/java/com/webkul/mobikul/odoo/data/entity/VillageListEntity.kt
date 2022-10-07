package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class VillageListEntity(
    val status: String?,
    @SerializedName("data")
    val villages: ArrayList<VillageEntity>
)
