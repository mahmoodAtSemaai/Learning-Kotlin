package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import android.content.Context
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.NetworkModelParser
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthServices
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
        private val apiService: AuthServices,
        private val context: Context
) : BaseRemoteDataSource() {

    suspend fun logIn() = safeApiCall {
        val response = apiService.signIn(RegisterDeviceTokenRequest(context).toString())
        NetworkModelParser(
                LoginEntity::class.java,
                LoginResponse::class.java
        ).toObject(response)
    }

    suspend fun signUp(signUpRequest: SignUpRequest) = safeApiCall {
        val response = apiService.signUp(signUpRequest.toString())
        NetworkModelParser(
                SignUpEntity::class.java,
                SignUpResponse::class.java
        ).toObject(response)

    }
}