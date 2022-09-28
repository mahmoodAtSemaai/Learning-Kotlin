package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class ProductCategoriesResponse(
        @SerializedName("categories") val categories: List<ProductCategoryResponse>
)
