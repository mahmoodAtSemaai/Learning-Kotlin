package com.webkul.mobikul.odoo.data.localSource.localDataSource

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.entity.OnboardingData
import com.webkul.mobikul.odoo.data.entity.OnboardingDataList
import com.webkul.mobikul.odoo.data.localSource.localDataSourceInterface.OnboardingDataCache
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
        private val resourcesProvider: ResourcesProvider
) : OnboardingDataCache {

    override suspend fun get(): Resource<OnboardingDataList> {
        return Resource.Success(getOnboadringDataList())
    }

    private fun getOnboadringDataList(): OnboardingDataList {
        val onboardingDataList = mutableListOf<OnboardingData>()
        resourcesProvider.apply {
            onboardingDataList.add(
                    OnboardingData(
                            getString(R.string.onboarding_title_1),
                            getString(R.string.onboarding_description_1),
                            getDrawableName(R.drawable.ic_onboarding_1)
                    )
            )
            onboardingDataList.add(
                    OnboardingData(
                            getString(R.string.onboarding_title_2),
                            getString(R.string.onboarding_description_2),
                            getDrawableName(R.drawable.ic_onboarding_2)
                    )
            )
            onboardingDataList.add(
                    OnboardingData(
                            getString(R.string.onboarding_title_3),
                            getString(R.string.onboarding_description_3),
                            getDrawableName(R.drawable.ic_onboarding_3)
                    )
            )
            onboardingDataList.add(
                    OnboardingData(
                            getString(R.string.onboarding_title_4),
                            getString(R.string.onboarding_description_4),
                            getDrawableName(R.drawable.ic_onboarding_4)
                    )
            )
            onboardingDataList.add(
                    OnboardingData(
                            getString(R.string.onboarding_title_5),
                            getString(R.string.onboarding_description_5),
                            getDrawableName(R.drawable.ic_onboarding_5)
                    )
            )
        }
        return OnboardingDataList(onboardingDataList)
    }
}