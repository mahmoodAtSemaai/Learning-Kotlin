package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.SerializedName

data class OnboardingStagesEntity(
    @SerializedName("onboardings") val stageDetails : List<StagesEntity>
)
