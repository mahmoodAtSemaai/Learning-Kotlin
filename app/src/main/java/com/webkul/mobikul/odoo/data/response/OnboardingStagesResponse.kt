package com.webkul.mobikul.odoo.data.response

import com.google.gson.annotations.SerializedName

data class OnboardingStagesResponse(
    @SerializedName("onboardings") val stageDetails: List<StagesResponse>
)
