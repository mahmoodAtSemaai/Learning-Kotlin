package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.SplashDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SplashServices
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import javax.inject.Inject

class SplashRemoteDataSource @Inject constructor(
        private val apiService: SplashServices,
        gson: Gson,
        val appPreferences: AppPreferences
) : SplashDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall(SplashScreenResponse::class.java) {
        apiService.getSplashPageData()
    }
}