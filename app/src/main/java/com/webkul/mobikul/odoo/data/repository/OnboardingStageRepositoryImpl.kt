package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OnboardingStagesEntity
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.OnboardingStageRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.OnboardingStageRepository
import javax.inject.Inject

class OnboardingStageRepositoryImpl @Inject constructor(
    private val remoteDataSource: OnboardingStageRemoteDataSource
) : OnboardingStageRepository {
    override suspend fun getUserOnboardingStage(userId: String): Resource<UserOnboardingStageEntity> {
        return remoteDataSource.getUserOnboardingStage(userId)
    }

    override suspend fun getOnboardingStages(): Resource<OnboardingStagesEntity> {
        return remoteDataSource.getOnboardingStages()
    }
}