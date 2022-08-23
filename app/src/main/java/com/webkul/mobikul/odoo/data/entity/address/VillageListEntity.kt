package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

data class VillageListEntity(
    @SerializedName("data") val villageList: List<VillageEntity>
)