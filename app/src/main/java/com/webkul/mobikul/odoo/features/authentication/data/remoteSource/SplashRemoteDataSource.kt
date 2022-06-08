package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class SplashRemoteDataSource @Inject constructor(private val splashServices: SplashServices) :
    BaseRemoteDataSource() {

    suspend fun getSplashPageData() = safeApiCall {
        splashServices.getSplashPageData()
    }

}