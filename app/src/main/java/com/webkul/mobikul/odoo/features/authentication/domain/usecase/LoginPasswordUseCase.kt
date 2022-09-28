package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import android.util.Base64
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.HTTP_RESPONSE_OK
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.LoginOtpAuthenticationRequest
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import com.webkul.mobikul.odoo.features.authentication.domain.repo.AuthenticationRepository
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val appPreferences: AppPreferences
) {

    operator fun invoke(loginOtpAuthenticationRequest: LoginOtpAuthenticationRequest)
            : Flow<Resource<BaseOtpLoginResponse<OtpAuthenticationResponse>>> = flow {
        emit(Resource.Loading)
        //TODO -> Handle preferences related stuff in data layer
        appPreferences.customerLoginToken = Base64.encodeToString(
            AuthenticationRequest(
                loginOtpAuthenticationRequest.login, loginOtpAuthenticationRequest.password
            ).toString().toByteArray(), Base64.NO_WRAP
        )

        val result = authenticationRepository.loginViaJWTToken(
            loginOtpAuthenticationRequest.login,
            loginOtpAuthenticationRequest
        )
        when (result) {
            is Resource.Default -> {}
            is Resource.Failure -> {
                setCustomerData("","", "")
                appPreferences.customerLoginToken = null
                emit(Resource.Failure(failureStatus = FailureStatus.API_FAIL, message = result.message))
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                if (result.value.statusCode.toInt() == HTTP_RESPONSE_OK) {
                    appPreferences.authToken = result.value.result.auth
                    appPreferences.customerId = result.value.result.customerId
                    appPreferences.userId = result.value.result.userId
                    emit(result)
                } else {
                    setCustomerData("","", "")
                    appPreferences.customerLoginToken = null
                    emit(Resource.Failure( failureStatus = FailureStatus.API_FAIL,  message = result.value.message ))
                }
            }
        }

    }.flowOn(Dispatchers.IO)

    private fun setCustomerData(authToken: String, customerId: String, userId : String) {
        appPreferences.authToken = authToken
        appPreferences.customerId = customerId
        appPreferences.userId = userId
    }

}