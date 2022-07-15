package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SplashServicesV1
import javax.inject.Inject

class SplashPageRemoteDataSource @Inject constructor(
        private val apiService: SplashServicesV1,
        gson: Gson,
        val appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getSplashPageData() = safeApiCall(SplashEntity::class.java) {
        apiService.getSplashPageData()
    }
}