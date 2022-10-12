package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class StateEntity(
    val id: Int = 0,

    val name: String? = null,

    @SerializedName("is_available")
    val available: Boolean? = false,

)
