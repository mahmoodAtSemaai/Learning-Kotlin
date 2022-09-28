package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val productServices: ProductServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(searchQuery: String) = safeApiCall(ProductListEntity::class.java) {
        productServices.getSearchProducts(searchQuery,true ,0, BuildConfig.DEFAULT_NO_OF_SEARCH_PRODUCTS)
    }

}