package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import android.content.Context
import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.LoginEntity
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AuthServices
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
        private val apiService: AuthServices,
        private val context: Context,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson,appPreferences) {

    suspend fun logIn() = safeApiCall(LoginEntity::class.java) {
        apiService.signIn(RegisterDeviceTokenRequest(context).toString())
    }

    suspend fun signUp(signUpRequest: SignUpRequest) = safeApiCall(SignUpEntity::class.java) {
        apiService.signUp(signUpRequest.toString())
    }
}