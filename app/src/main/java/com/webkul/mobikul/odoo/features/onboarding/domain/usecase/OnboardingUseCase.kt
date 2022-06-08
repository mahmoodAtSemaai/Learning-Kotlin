package com.webkul.mobikul.odoo.features.onboarding.domain.usecase

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.features.onboarding.data.models.OnboardingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OnboardingUseCase @Inject constructor(
        private val resourcesProvider: ResourcesProvider) {

    operator fun invoke(): Flow<Resource<MutableList<OnboardingData>>> = flow {
        val onboardingDataList = mutableListOf<OnboardingData>()
        onboardingDataList.add(OnboardingData(
                resourcesProvider.getString(R.string.onboarding_title_1),
                resourcesProvider.getString(R.string.onboarding_description_1),
                resourcesProvider.getDrawabale(R.drawable.ic_onboarding_1)))
        onboardingDataList.add(OnboardingData(
                resourcesProvider.getString(R.string.onboarding_title_2),
                resourcesProvider.getString(R.string.onboarding_description_2),
                resourcesProvider.getDrawabale(R.drawable.ic_onboarding_2)))
        onboardingDataList.add(OnboardingData(
                resourcesProvider.getString(R.string.onboarding_title_3),
                resourcesProvider.getString(R.string.onboarding_description_3),
                resourcesProvider.getDrawabale(R.drawable.ic_onboarding_3)))
        onboardingDataList.add(OnboardingData(
                resourcesProvider.getString(R.string.onboarding_title_4),
                resourcesProvider.getString(R.string.onboarding_description_4),
                resourcesProvider.getDrawabale(R.drawable.ic_onboarding_4)))
        onboardingDataList.add(OnboardingData(
                resourcesProvider.getString(R.string.onboarding_title_5),
                resourcesProvider.getString(R.string.onboarding_description_5),
                resourcesProvider.getDrawabale(R.drawable.ic_onboarding_5)))

        emit(Resource.Success(onboardingDataList))

    }.flowOn(Dispatchers.IO)
}