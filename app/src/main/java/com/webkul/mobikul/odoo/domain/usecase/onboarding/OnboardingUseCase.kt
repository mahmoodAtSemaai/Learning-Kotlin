package com.webkul.mobikul.odoo.domain.usecase.onboarding

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OnboardingData
import com.webkul.mobikul.odoo.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): MutableList<OnboardingData> {
        return when(val result = onboardingRepository.get()){
            is Resource.Success -> result.value.onboardingDataList
            else -> mutableListOf()
        }
    }
}