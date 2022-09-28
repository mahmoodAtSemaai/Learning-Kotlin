package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ProductCategoriesEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.CategoriesServices
import javax.inject.Inject

class CategoriesRemoteDataSource @Inject constructor(
        private val categoriesServices: CategoriesServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get() = safeApiCall(ProductCategoriesEntity::class.java) {
        categoriesServices.getProductCategories()
    }

}