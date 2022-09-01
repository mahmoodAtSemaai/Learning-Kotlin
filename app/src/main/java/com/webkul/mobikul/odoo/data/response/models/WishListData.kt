package com.webkul.mobikul.odoo.data.response.models

import com.google.gson.annotations.SerializedName

data class WishListData(
    @SerializedName("name")
    var name: String,

    @SerializedName("priceUnit")
    var priceUnit: String,

    @SerializedName("priceReduce")
    var priceReduce: String,

    @SerializedName("thumbNail")
    var thumbNail: String,

    @SerializedName("wishlist_id")
    var wishlistId: Int,

    @SerializedName("product_id")
    var productId: Int,

    @SerializedName("templateId")
    var templateId: Int,
)
