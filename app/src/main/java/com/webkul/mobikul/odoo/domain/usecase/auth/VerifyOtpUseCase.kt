package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.request.OtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPException
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPValidation
import com.webkul.mobikul.odoo.domain.repository.AuthenticationOtpRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authenticationOtpRepository: AuthenticationOtpRepository,
    private val userRepository: UserRepository
) {

    @Throws(VerifyOTPException::class)
    operator fun invoke(otpAuthenticationRequest: OtpAuthenticationRequest)
            : Flow<Resource<OtpAuthenticationEntity>> = flow {
        if (otpAuthenticationRequest.otp.trim().length < 4)
            emit(Resource.Default)
        else {
            emit(Resource.Loading)
            val result =
                authenticationOtpRepository.loginViaOtpJWTToken(otpAuthenticationRequest)
            when (result) {
                is Resource.Success -> {
                    userRepository.update(
                        UserRequest(
                            customerJWTToken = result.value.auth,
                            customerId = result.value.customerId
                        )
                    )
                }
                is Resource.Failure -> {
                    userRepository.update(
                        UserRequest(customerJWTToken = "", customerId = "", customerLoginToken = "")
                    )
                    if (result.failureStatus == FailureStatus.RESOURCE_NOT_FOUND) {
                        throw VerifyOTPException(VerifyOTPValidation.INVALID_OTP.value.toString())
                    }
                }
            }
            emit(result)
        }
    }.flowOn(Dispatchers.IO)

}