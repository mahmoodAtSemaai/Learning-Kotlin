package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.AppConfigRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsFirstTimeUseCase @Inject constructor(
        private val appConfigRepository: AppConfigRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {

        try {
            delay((ApplicationConstant.MILLIS / 2).toLong())
            when (val result = appConfigRepository.get()) {
                is Resource.Success -> {
                    emit(Resource.Success(result.value.isAppRunFirstTime))
                }
                is Resource.Failure -> emit(Resource.Failure(result.failureStatus, result.code, result.message))
                is Resource.Loading -> Resource.Loading
                is Resource.Default -> Resource.Default
            }
        } catch (e: Exception) {
            emit(Resource.Failure(FailureStatus.OTHER))
        }
    }
}