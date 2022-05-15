package com.webkul.mobikul.odoo.features.auth.data.repo

import android.util.Base64
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource,
    private val appPreferences: AppPreferences
) : LoginRepository {

    override
    suspend fun logIn(username: String, password: String): Resource<LoginResponse> {

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
            else -> {
                if(result is Resource.Success && !result.value.isSuccess){
                    appPreferences.customerLoginToken = null
                }
            }
        }

        return result
    }
}