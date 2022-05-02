package com.webkul.mobikul.odoo.features.auth.data.repo

import android.util.Base64
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.SignUpRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val remoteDataSource: SignUpRemoteDataSource,
    private val appPreferences: AppPreferences
) : SignUpRepository {

    override
    suspend fun signUp(signUpRequest: SignUpRequest): Resource<SignUpResponse> {
        appPreferences.customerLoginToken = Base64.encodeToString(
            AuthenticationRequest(
                signUpRequest.login, signUpRequest.password
            ).toString().toByteArray(), Base64.NO_WRAP
        )
        return remoteDataSource.signUp(signUpRequest)
    }
}