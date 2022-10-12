package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShippingMethodEntity(
    @SerializedName("price")
    @Expose
    val price: String? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("id")
    @Expose
    val id: Int = 0
)
