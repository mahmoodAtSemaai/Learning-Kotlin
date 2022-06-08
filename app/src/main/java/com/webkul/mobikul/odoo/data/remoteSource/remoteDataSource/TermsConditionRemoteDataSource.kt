package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.NetworkModelParser
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.TermsConditionServices
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import javax.inject.Inject

class TermsConditionRemoteDataSource @Inject constructor(
        private val apiService: TermsConditionServices
) : BaseRemoteDataSource() {

    suspend fun getTermAndCondition() = safeApiCall {
        val response = apiService.getTermAndCondition()
        NetworkModelParser(
                TermsAndConditionsEntity::class.java,
                TermAndConditionResponse::class.java
        ).toObject(response)
    }

    suspend fun getSellerTerms() = safeApiCall {
        val response = apiService.getSellerTerms()
        NetworkModelParser(
                TermsAndConditionsEntity::class.java,
                TermAndConditionResponse::class.java
        ).toObject(response)
    }
}