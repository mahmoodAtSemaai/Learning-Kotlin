package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import javax.inject.Inject

class GetOnboardingDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val resourcesProvider: ResourcesProvider
) {
    suspend operator fun invoke(): Resource<List<String>> {
        when (val user = userRepository.get()) {
            is Resource.Success -> {
                return if ((user.value.customerId.toString()
                        .isEmpty()) || (user.value.userId.isBlank())
                ) {
                    Resource.Failure(
                        FailureStatus.OTHER,
                        null,
                        resourcesProvider.getString(R.string.error_something_went_wrong)
                    )
                } else {
                    val onBoardingList = listOf(
                        user.value.userId,
                        user.value.customerId.toString(),
                        user.value.customerGroupName,
                        user.value.customerGrpId.toString()
                    )
                    Resource.Success(onBoardingList)
                }
            }
            else -> {
                return Resource.Failure(
                    FailureStatus.OTHER,
                    null,
                    resourcesProvider.getString(R.string.error_something_went_wrong)
                )
            }
        }

    }
}