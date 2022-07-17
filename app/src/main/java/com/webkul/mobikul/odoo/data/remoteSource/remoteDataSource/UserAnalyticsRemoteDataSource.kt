package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.UserAnalyticsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SplashServicesV1
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import javax.inject.Inject

class UserAnalyticsRemoteDataSource @Inject constructor(
        private val apiService: SplashServicesV1,
        private val context: Context,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun getUserAnalytics() = safeApiCall(UserAnalyticsEntity::class.java) {
        apiService.getUserAnalyticsDetails(RegisterDeviceTokenRequest(context).toString())

    }
}