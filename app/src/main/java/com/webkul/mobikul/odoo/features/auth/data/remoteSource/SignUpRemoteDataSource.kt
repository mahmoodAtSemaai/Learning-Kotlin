package com.webkul.mobikul.odoo.features.auth.data.remoteSource

import android.content.Context
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import javax.inject.Inject

class SignUpRemoteDataSource @Inject constructor(private val apiService: AuthServices , private val context: Context) : BaseRemoteDataSource() {

    suspend fun signUp(signUpRequest: SignUpRequest) = safeApiCall {
        apiService.signUp(signUpRequest.toString())
    }
}