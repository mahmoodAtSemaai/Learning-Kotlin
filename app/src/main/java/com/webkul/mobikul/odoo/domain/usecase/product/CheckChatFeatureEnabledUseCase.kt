package com.webkul.mobikul.odoo.domain.usecase.product

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckChatFeatureEnabledUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {

    operator fun invoke(): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading)
        try {
            val result = appPreferences.isSeller.not() && FirebaseRemoteConfigHelper.isChatFeatureEnabled
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Failure(FailureStatus.API_FAIL))
        }
    }
}