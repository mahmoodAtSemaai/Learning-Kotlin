package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ReferralCodeServices
import javax.inject.Inject

class ReferralCodeRemoteDataSource @Inject constructor(
    private val apiService: ReferralCodeServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun validateReferralCode(referralCode: String) = safeApiCall(Any::class.java) {
        apiService.validateReferralCode(referralCode)
    }

}