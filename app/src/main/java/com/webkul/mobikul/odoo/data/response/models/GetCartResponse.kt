package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.data.entity.SellerEntity

data class GetCartResponse(
    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("cart_order_id")
    @Expose
    val orderId: Int,

    @SerializedName("cart_count")
    @Expose
    val newCartCount: Int,


    @SerializedName("sellers")
    @Expose
    val seller: ArrayList<SellerEntity>?
)