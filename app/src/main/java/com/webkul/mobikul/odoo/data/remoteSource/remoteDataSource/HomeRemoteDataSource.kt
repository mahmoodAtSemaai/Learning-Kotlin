package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.HomeServicesV1
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
        private val apiService: HomeServicesV1,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    //TODO->Handle homepage Response with home revamp
    suspend fun getHomePageData() = safeApiCall {
        apiService.getHomePageData()
    }
}