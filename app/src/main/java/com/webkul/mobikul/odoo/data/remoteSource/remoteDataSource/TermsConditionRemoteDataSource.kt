package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.TermsConditionDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
import javax.inject.Inject

class TermsConditionRemoteDataSource @Inject constructor(
        private val apiService: TermsConditionServices,
        gson: Gson,
        appPreferences: AppPreferences
) : TermsConditionDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall {
        apiService.getTermAndCondition()
    }
}