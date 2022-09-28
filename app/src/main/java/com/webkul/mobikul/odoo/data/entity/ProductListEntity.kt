package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class ProductListEntity(
        @SerializedName("offset") var offset: Int = 0,
        @SerializedName("tcount") var tCount: Int = 0,
        @SerializedName("products") var products: ArrayList<ProductEntity> = ArrayList(),
        var lazyLoading:Boolean = false//TODO Check from $CatalogProductResponse
)
