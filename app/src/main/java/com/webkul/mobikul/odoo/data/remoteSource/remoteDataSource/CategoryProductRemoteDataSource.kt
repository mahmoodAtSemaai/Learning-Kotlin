package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices
import javax.inject.Inject

class CategoryProductRemoteDataSource @Inject constructor(
    private val productServices: ProductServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(categoryId: Int ,globalProductsEnabled : Boolean, offset: Int, limit: Int) =
        safeApiCall(ProductListEntity::class.java) {
            productServices.getCategoryProducts(categoryId,globalProductsEnabled, offset, limit)
        }

}