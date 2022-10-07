package com.webkul.mobikul.odoo.data.localSource.localDataSource

import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.request.AppConfigRequest
import com.webkul.mobikul.odoo.data.response.AppConfigResponse
import javax.inject.Inject

class AppConfigLocalDataSource @Inject constructor(
        private val appPreferences: AppPreferences
) : IDataSource<AppConfigResponse, String, AppConfigRequest> {

    override suspend fun get(): Resource<AppConfigResponse> {
        return Resource.Success(getAppConfig())
    }

    private fun getAppConfig(): AppConfigResponse {
        return AppConfigResponse(
                isAppRunFirstTime = appPreferences.isFirstTime,
                isAllowGuestCheckout = appPreferences.isAllowedGuestCheckout
        )
    }

    override suspend fun update(request: AppConfigRequest): Resource<AppConfigResponse> {
        return Resource.Success(updateAppConfig(request))
    }


    private fun updateAppConfig(request: AppConfigRequest): AppConfigResponse {
        request.isAppRunFirstTime?.let { appPreferences.isFirstTime = it }
        request.isAllowGuestCheckout?.let { appPreferences.isAllowedGuestCheckout = it }
        return getAppConfig()
    }
}