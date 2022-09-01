package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishListEntity(
    @SerializedName("name")
    @Expose
    private var name: String,

    @SerializedName("priceUnit")
    @Expose
    private val priceUnit: String,

    @SerializedName("priceReduce")
    @Expose
    private val priceReduce: String?,

    @SerializedName("id")
    @Expose
    private val id: String,

    @SerializedName("thumbNail")
    @Expose
    private val thumbNail: String,

    @SerializedName("productId")
    @Expose
    private val productId: String,

    @SerializedName("templateId")
    @Expose
    private val templateId: String,
)
