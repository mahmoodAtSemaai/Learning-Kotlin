package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishList(
    @SerializedName("wishLists")
    @Expose
    val wishlistItems: ArrayList<WishListEntity>
)