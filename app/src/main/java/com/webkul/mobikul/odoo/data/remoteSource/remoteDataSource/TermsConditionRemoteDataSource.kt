package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
import javax.inject.Inject

class TermsConditionRemoteDataSource @Inject constructor(
        private val apiService: TermsConditionServices, gson: Gson
) : BaseRemoteDataSource(gson) {

    suspend fun getTermAndCondition() = safeApiCall(TermsAndConditionsEntity::class.java) {
        apiService.getTermAndCondition()
    }

    suspend fun getSellerTerms() = safeApiCall(TermsAndConditionsEntity::class.java) {
        apiService.getSellerTerms()
    }
}