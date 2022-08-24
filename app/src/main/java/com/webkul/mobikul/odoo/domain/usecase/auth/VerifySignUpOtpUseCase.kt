package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_BAD_REQUEST
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPException
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPValidation
import com.webkul.mobikul.odoo.domain.repository.SignUpAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifySignUpOtpUseCase @Inject constructor(
    private val signUpAuthRepository: SignUpAuthRepository
) {
    @Throws(VerifyOTPException::class)
    operator fun invoke(signUpOtpAuthRequest: SignUpOtpAuthRequest): Flow<Resource<BaseOtpSignUpResponse<SignUpV1Entity>>> =
        flow {

            emit(Resource.Loading)
            val result = signUpAuthRepository.loginViaOtpJWTToken(signUpOtpAuthRequest)
            when (result) {
                is Resource.Failure -> {
                    if (result.code ?: 0 >= HTTP_ERROR_BAD_REQUEST) {
                        throw VerifyOTPException(VerifyOTPValidation.INVALID_OTP.value.toString())
                    }
                }
            }
            emit(result)

        }.flowOn(Dispatchers.IO)
}