package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAnalyticsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserAnalyticsDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SplashServices
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class UserAnalyticsRemoteDataSource @Inject constructor(
        private val apiService: SplashServices,
        private val context: Context,
        gson: Gson,
        appPreferences: AppPreferences
) : UserAnalyticsDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall(UserAnalyticsResponse::class.java) {
        apiService.getUserAnalyticsDetails(RegisterDeviceTokenRequest(context).toString())
    }

}