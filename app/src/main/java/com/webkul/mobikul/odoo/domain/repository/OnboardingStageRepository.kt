package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity

interface OnboardingStageRepository : Repository {
    suspend fun getUserOnboardingStage(userId: String): Resource<UserOnboardingStageEntity>

    suspend fun getOnboardingStages() : Resource<OnboardingStagesEntity>
}