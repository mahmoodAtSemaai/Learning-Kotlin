package com.webkul.mobikul.odoo.features.auth.domain.usecase

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.enums.LogInValidationException
import com.webkul.mobikul.odoo.features.auth.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {

    @Throws(LogInValidationException::class)
    operator fun invoke(username: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)

        if (isValidLogin(username, password)) {
            val result = loginRepository.logIn(username, password)

            //emit(result)
            when (result) {
                is Resource.Success -> {
                    if (result.value.isSuccess) emit(result)
                    else emit(Resource.Failure( failureStatus = FailureStatus.API_FAIL , message = result.value.message))
                  //  else throw LogInValidationException(LoginFieldsValidation.INVALID_LOGIN_DETAILS.value.toString())
                }
                else -> emit(result)
            }

        } else {

            if (username.isEmpty()) throw LogInValidationException(LoginFieldsValidation.EMPTY_EMAIL.value.toString())

            if (password.isEmpty()) throw LogInValidationException(LoginFieldsValidation.EMPTY_PASSWORD.value.toString())

            if (password.length < BuildConfig.MIN_PASSWORD_LENGTH) throw LogInValidationException(
                LoginFieldsValidation.INVALID_PASSWORD.value.toString()
            )

        }
    }.flowOn(Dispatchers.IO)

    private fun isValidLogin(username: String, password: String): Boolean {
        if (username.isNotEmpty() && password.isNotEmpty() && password.length > BuildConfig.MIN_PASSWORD_LENGTH) return true
        return false
    }

}