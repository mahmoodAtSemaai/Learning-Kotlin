package com.webkul.mobikul.odoo.features.auth.domain.usecase

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject

class LogInUseCase @Inject constructor(
        private val loginRepository: LoginRepository,
) {

    fun login(username: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)

        val result = loginRepository.logIn()
        emit(result)
    }.flowOn(Dispatchers.IO)

    private fun isValidLogin(username: String, password: String) : Boolean{
        //Login Credential Validations.
        return false
    }

}