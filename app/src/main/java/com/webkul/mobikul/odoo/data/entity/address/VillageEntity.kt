package com.webkul.mobikul.odoo.data.entity.address

import com.google.gson.annotations.SerializedName

class VillageEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("zip") val zip: String,
    @SerializedName("is_available") val isAvailable: Boolean
)