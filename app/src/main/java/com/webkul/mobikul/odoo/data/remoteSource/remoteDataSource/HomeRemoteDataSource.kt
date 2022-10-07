package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.HomeDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.HomeServices
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
        private val apiService: HomeServices,
        gson: Gson,
        appPreferences: AppPreferences
) : HomeDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall {
        apiService.getHomePageData()
    }

}