package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import javax.inject.Inject

class GetOnboardingDataUseCase @Inject constructor(
    private val appPreferences: AppPreferences,
    private val resourcesProvider: ResourcesProvider
) {
    operator fun invoke(): Resource<List<String>> {
        val customerId = appPreferences.customerId.toString()
        val userId = appPreferences.userId
        val groupName = appPreferences.groupName
        val customerGroupId = appPreferences.customerGroupId
        return if ((customerId.isEmpty()) || (userId.isNullOrBlank())) {
            Resource.Failure(
                FailureStatus.OTHER,
                null,
                resourcesProvider.getString(R.string.error_something_went_wrong)
            )
        } else {
            val onBoardingList = listOf(userId.toString(), customerId, groupName, customerGroupId.toString())
            Resource.Success(onBoardingList)
        }
    }
}