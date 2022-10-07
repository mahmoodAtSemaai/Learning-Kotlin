package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.UserOnboardingStageDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.UserServices
import com.webkul.mobikul.odoo.data.response.UserOnboardingStageResponse
import javax.inject.Inject

class UserOnboardingStageRemoteDataSource @Inject constructor(
    private val apiService: UserServices,
    gson: Gson,
    appPreferences: AppPreferences
) : UserOnboardingStageDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun getById(id: String) =
        safeApiCall(UserOnboardingStageResponse::class.java) {
            apiService.getUserOnboardingStage(id)
        }
}