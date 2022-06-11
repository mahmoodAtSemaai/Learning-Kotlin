package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class SplashRemoteDataSource @Inject constructor(
        private val splashServices: SplashServices,
        gson: Gson
) : BaseRemoteDataSource(gson) {

    suspend fun getSplashPageData() = safeApiCall {
        splashServices.getSplashPageData()
    }

}