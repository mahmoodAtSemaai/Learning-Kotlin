package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {
    operator fun invoke(): Resource<Boolean> {

        return try {
            val result = appPreferences.isLoggedIn
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Failure(FailureStatus.ACCESS_DENIED)
        }
    }
}