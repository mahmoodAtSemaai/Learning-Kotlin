package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class ProductCategoriesEntity(
        @SerializedName("categories") val categories: List<ProductCategoryEntity>
)
