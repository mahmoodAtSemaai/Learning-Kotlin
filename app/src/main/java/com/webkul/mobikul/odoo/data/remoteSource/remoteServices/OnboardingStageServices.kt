package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface OnboardingStageServices {

    @GET(ApiInterface.USER_ONBOARDING_STAGE)
    suspend fun getUserOnboardingStage(@Query(ApiInterface.USER_ID) userId: String): BaseUserOnboardingResponse<UserOnboardingStageEntity>

    @GET(ApiInterface.ONBOARDING_STAGES)
    suspend fun getOnboardingStages(): BaseUserOnboardingResponse<OnboardingStagesEntity>
}