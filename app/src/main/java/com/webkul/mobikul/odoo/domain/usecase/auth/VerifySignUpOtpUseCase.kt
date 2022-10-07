package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_BAD_REQUEST
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.request.SignUpOtpAuthRequest
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPException
import com.webkul.mobikul.odoo.domain.enums.VerifyOTPValidation
import com.webkul.mobikul.odoo.domain.repository.SignUpAuthRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifySignUpOtpUseCase @Inject constructor(
    private val signUpAuthRepository: SignUpAuthRepository,
    private val userRepository: UserRepository
) {
    @Throws(VerifyOTPException::class)
    operator fun invoke(signUpOtpAuthRequest: SignUpOtpAuthRequest): Flow<Resource<SignUpV1Entity>> =
        flow {

            emit(Resource.Loading)
            val result = signUpAuthRepository.create(signUpOtpAuthRequest)
            when (result) {
                is Resource.Success -> {
                    userRepository.update(
                        UserRequest(
                            customerJWTToken = result.value.auth,
                            customerId = result.value.customerId,
                            userId = result.value.userId
                        )
                    )
                }
                is Resource.Failure -> {
                    UserRequest(
                        customerLoginToken = "",
                        customerJWTToken = "",
                        customerId = "",
                        userId = ""
                    )
                    if ((result.code ?: 0) >= HTTP_ERROR_BAD_REQUEST) {
                        throw VerifyOTPException(VerifyOTPValidation.INVALID_OTP.value.toString())
                    }
                }
            }
            emit(result)

        }.flowOn(Dispatchers.IO)
}