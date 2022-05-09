package com.webkul.mobikul.odoo.features.auth.data.repo

import android.util.Base64
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.LoginRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.data.remoteSource.SignUpRemoteDataSource
import com.webkul.mobikul.odoo.features.auth.domain.repo.LoginRepository
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
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
        val result = remoteDataSource.signUp(signUpRequest)

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

    override suspend fun getAddressBookData(baseLazyRequest: BaseLazyRequest): Resource<MyAddressesResponse> {
        return remoteDataSource.getAddressBookData(baseLazyRequest)
    }

    override suspend fun getCountryStateData(): Resource<CountryStateData> {
        return remoteDataSource.getCountryStateData()
    }

    override suspend fun getTermAndCondition(): Resource<TermAndConditionResponse> {
        return remoteDataSource.getTermAndCondition()
    }

    override suspend fun getSellerTerms(): Resource<TermAndConditionResponse> {
        return remoteDataSource.getSellerTerms()
    }


}