package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.OnboardingStageDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.OnboardingStageServices
import com.webkul.mobikul.odoo.data.response.OnboardingStagesResponse
import javax.inject.Inject

class OnboardingStageRemoteDataSource @Inject constructor(
    private val apiService: OnboardingStageServices,
    gson: Gson,
    appPreferences: AppPreferences
) : OnboardingStageDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall(OnboardingStagesResponse::class.java) {
        apiService.getOnboardingStages()
    }
}