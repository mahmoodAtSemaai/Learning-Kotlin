package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class ProductCategoryResponse(
        @SerializedName("category_id") val categoryId : Int,
        @SerializedName("name") val name : String,
        @SerializedName("type") val type : String,
        @SerializedName("children") val children : List<ProductCategoryResponse>,
        @SerializedName("icon") val icon: String
)
