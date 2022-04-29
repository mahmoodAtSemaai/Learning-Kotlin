package com.webkul.mobikul.odoo.features.auth.domain.usecase

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.utils.Resource
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

    lateinit var errorMessage:String

    operator fun invoke(username: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)

        if (isValidLogin(username, password)) {
            val result = loginRepository.logIn()
            emit(result)
        } else {

        }
    }.flowOn(Dispatchers.IO)

    private fun isValidLogin(username: String, password: String): Boolean {

        if (username.isNotEmpty() && password.isNotEmpty() && password.length > BuildConfig.MIN_PASSWORD_LENGTH)
            return true
        else {



        }

            return false
    }

}