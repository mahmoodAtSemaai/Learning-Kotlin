package com.webkul.mobikul.odoo.data.repository

import android.util.Base64
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AuthRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.AuthRepository
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
        private val remoteDataSource: AuthRemoteDataSource,
        private val appPreferences: AppPreferences
) : AuthRepository {

    override suspend fun logIn(username: String, password: String): Resource<LoginEntity> {

        appPreferences.customerLoginToken = Base64.encodeToString(
                AuthenticationRequest(
                        username, password
                ).toString().toByteArray(), Base64.NO_WRAP
        )

        val result = remoteDataSource.logIn()

        when (result) {
            is Resource.Failure -> {
                appPreferences.customerLoginToken = null
            }
        }

        return result
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpEntity> {
        appPreferences.customerLoginToken = Base64.encodeToString(
                AuthenticationRequest(
                        signUpRequest.login, signUpRequest.password
                ).toString().toByteArray(), Base64.NO_WRAP
        )
        val result = remoteDataSource.signUp(signUpRequest)

        when (result) {
            is Resource.Failure -> {
                appPreferences.customerLoginToken = null
            }
        }

        return result
    }
}