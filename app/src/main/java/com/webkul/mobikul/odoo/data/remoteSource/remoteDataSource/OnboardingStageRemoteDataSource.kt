package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.OnboardingStageServices
import javax.inject.Inject

class OnboardingStageRemoteDataSource @Inject constructor(
    private val apiService: OnboardingStageServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {
    suspend fun getUserOnboardingStage(userId: String) = safeApiCall(UserOnboardingStageEntity::class.java) {
        apiService.getUserOnboardingStage(userId)
    }

    suspend fun getOnboardingStages() = safeApiCall(OnboardingStagesEntity::class.java){
        apiService.getOnboardingStages()
    }
}