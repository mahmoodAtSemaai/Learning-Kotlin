package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.HTTP_RESPONSE_OK
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val appPreferences: AppPreferences
) {

    operator fun invoke(phoneNumber: String, otpAuthenticationRequest: OtpAuthenticationRequest)
            : Flow<Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>>> = flow {

        emit(Resource.Loading)

        val result =
            authenticationRepository.loginViaOtpJWTToken(phoneNumber, otpAuthenticationRequest)
        when (result) {
            is Resource.Default -> {}
            is Resource.Failure -> {
                emit(Resource.Failure(failureStatus = FailureStatus.API_FAIL, message = result.message))
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                if (result.value.statusCode.toInt() == HTTP_RESPONSE_OK) {
                    appPreferences.authToken = result.value.result.auth
                    appPreferences.customerId = result.value.result.customerId
                    appPreferences.userId = result.value.result.userId.toInt()
                    emit(result)
                } else
                    emit(Resource.Failure(failureStatus = FailureStatus.API_FAIL, message = result.value.message))
            }
        }

    }.flowOn(Dispatchers.IO)

}