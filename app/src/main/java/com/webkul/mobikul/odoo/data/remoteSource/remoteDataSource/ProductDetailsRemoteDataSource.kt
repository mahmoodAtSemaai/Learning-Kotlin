package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices
import javax.inject.Inject

class ProductDetailsRemoteDataSource @Inject constructor(
        private val productServices: ProductServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(templateId: Int) = safeApiCall(ProductEntity::class.java) {
        productServices.getProductDetails(templateId)
    }

}