package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class VillageEntity(
    val id: Int = 0,

    val name: String? = null,

    val zip: String? = null,
    
    @SerializedName("is_available")
    val available: Boolean? = false,
)
