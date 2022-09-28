package com.webkul.mobikul.odoo.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCategoryEntity(
        @SerializedName("category_id") val categoryId : Int,
        @SerializedName("name") val name : String,
        @SerializedName("type") val type : String,
        @SerializedName("children") val children : List<ProductCategoryEntity>,
        @SerializedName("icon") val icon: String
): Parcelable
