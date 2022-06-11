package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class HomePageRemoteDataSource @Inject constructor(
        private val apiService: HomePageServices,
        gson: Gson
) : BaseRemoteDataSource(gson) {

    suspend fun getHomePageData() = safeApiCall {
        apiService.getHomePageData()
    }


}