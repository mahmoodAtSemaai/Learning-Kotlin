package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.domain.enums.LogInValidationException
import com.webkul.mobikul.odoo.domain.enums.LoginFieldsValidation
import com.webkul.mobikul.odoo.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogInUseCase @Inject constructor(
        private val authRepository: AuthRepository,
) {

    @Throws(LogInValidationException::class)
    operator fun invoke(username: String, password: String): Flow<Resource<LoginEntity>> = flow {
        emit(Resource.Loading)

        if (isValidLogin(username, password)) {
            val result = authRepository.logIn(username, password)
            emit(result)

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