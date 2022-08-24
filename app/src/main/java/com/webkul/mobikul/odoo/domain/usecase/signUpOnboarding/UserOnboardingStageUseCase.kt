package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import com.webkul.mobikul.odoo.domain.enums.ExistingUserValidationException
import com.webkul.mobikul.odoo.domain.repository.OnboardingStageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserOnboardingStageUseCase @Inject constructor(
    private val onboardingStageRepository: OnboardingStageRepository
) {
    @Throws(ExistingUserValidationException::class)
    operator fun invoke(userId: String): Flow<Resource<UserOnboardingStageEntity>> =
        flow {

            emit(Resource.Loading)
            val result = onboardingStageRepository.getUserOnboardingStage(userId)
            emit(result)

        }.flowOn(Dispatchers.IO)
}