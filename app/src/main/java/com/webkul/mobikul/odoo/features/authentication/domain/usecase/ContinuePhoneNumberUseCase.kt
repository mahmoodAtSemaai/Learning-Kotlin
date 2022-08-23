package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.HTTP_RESPONSE_OK
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinuePhoneNumberUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    operator fun invoke(phoneNumber: String): Flow<Resource<BaseOtpLoginResponse<UserCreateDateEntity>>> = flow {
        emit(Resource.Loading)
        val result = authenticationRepository.validatePhoneNumber(phoneNumber)
        when (result) {
            is Resource.Default -> {}
            is Resource.Failure -> {
                emit(Resource.Failure(failureStatus = FailureStatus.API_FAIL, message = result.message))
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                if (result.value.statusCode == HTTP_RESPONSE_OK.toString())
                    emit(result)
                else
                    emit(Resource.Failure(failureStatus = FailureStatus.API_FAIL, message = result.value.message))
            }
        }

    }.flowOn(Dispatchers.IO)

}