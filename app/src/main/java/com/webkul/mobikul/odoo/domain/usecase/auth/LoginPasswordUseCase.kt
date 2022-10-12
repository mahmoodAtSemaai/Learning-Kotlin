package com.webkul.mobikul.odoo.domain.usecase.auth

import android.util.Base64
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.OtpAuthenticationEntity
import com.webkul.mobikul.odoo.data.request.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.data.request.UserRequest
import com.webkul.mobikul.odoo.domain.repository.AuthenticationPasswordRepository
import com.webkul.mobikul.odoo.domain.repository.UserRepository
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import com.webkul.mobikul.odoo.ui.authentication.domain.enums.VerifyPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginPasswordUseCase @Inject constructor(
        private val authenticationPasswordRepository: AuthenticationPasswordRepository,
        private val userRepository: UserRepository
) {

    @Throws(VerifyPasswordException::class)
    operator fun invoke(loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest)
            : Flow<Resource<OtpAuthenticationEntity>> = flow {
        emit(Resource.Loading)
        userRepository.update(
            UserRequest(
                customerLoginToken =
                Base64.encodeToString(
                    AuthenticationRequest(
                        loginOtpAuthenticationRequest.login, loginOtpAuthenticationRequest.password
                    ).toString().toByteArray(), Base64.NO_WRAP
                )
            )
        )
        val result = authenticationPasswordRepository.loginViaJWTToken(loginOtpAuthenticationRequest)
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
                if (result.failureStatus == FailureStatus.UNAUTHORISED_REQUEST) {
                    throw VerifyPasswordException(result.message.toString())
                }
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)


}