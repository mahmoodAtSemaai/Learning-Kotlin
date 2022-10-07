package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.SellerTermsConditionDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
import javax.inject.Inject

class SellerTermsConditionRemoteDataSource @Inject constructor(
        private val apiService: TermsConditionServices,
        gson: Gson,
        appPreferences: AppPreferences
) : SellerTermsConditionDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall {
        apiService.getSellerTerms()
    }



}