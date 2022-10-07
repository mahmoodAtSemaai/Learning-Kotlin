package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.response.OnboardingStagesResponse
import retrofit2.http.GET

interface OnboardingStageServices {

    @GET(ApiInterface.ONBOARDING_STAGES)
    suspend fun getOnboardingStages(): BaseUserOnboardingResponse<OnboardingStagesResponse>
}