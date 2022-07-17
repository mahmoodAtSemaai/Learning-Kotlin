package com.webkul.mobikul.odoo.domain.usecase.analytics

import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserAnalyticsEntity
import com.webkul.mobikul.odoo.domain.repository.UserAnalyticsRepository
import com.webkul.mobikul.odoo.model.user.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserAnalyticsUseCase @Inject constructor(
        private val userAnalyticsRepository: UserAnalyticsRepository,
        private val appPreferences: AppPreferences,
        private val firebaseAnalyticsImpl: FirebaseAnalyticsImpl
) {

    operator fun invoke(): Flow<Resource<UserAnalyticsEntity>> = flow {

        emit(Resource.Loading)
        firebaseAnalyticsImpl.logAppOpenEvent()
        if (appPreferences.isLoggedIn && appPreferences.analyticsId.isNullOrEmpty()) {
            when (val result = userAnalyticsRepository.getUserAnalytics()) {
                is Resource.Default -> emit(result)
                is Resource.Failure -> {
                    AnalyticsImpl.trackAnalyticsFailure()
                    emit(result)
                }
                is Resource.Loading -> emit(result)
                is Resource.Success -> {
                    AnalyticsImpl.initUserTracking(
                            UserModel(
                                    result.value.email,
                                    result.value.analyticsId,
                                    result.value.name,
                                    result.value.isSeller
                            )
                    )
                    appPreferences.analyticsId = result.value.analyticsId
                    emit(result)
                }
            }
        } else {
            emit(Resource.Failure(FailureStatus.OTHER))
        }

    }.flowOn(Dispatchers.IO)
}