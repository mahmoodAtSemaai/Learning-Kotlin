package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.TermsConditionRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.TermsConditionRepository
import javax.inject.Inject

class TermsConditionRepositoryImpl @Inject constructor(
        private val remoteDataSource: TermsConditionRemoteDataSource
) : TermsConditionRepository {

    override suspend fun getTermAndCondition(): Resource<TermsAndConditionsEntity> {
        return remoteDataSource.getTermAndCondition()
    }

    override suspend fun getSellerTerms(): Resource<TermsAndConditionsEntity> {
        return remoteDataSource.getSellerTerms()
    }
}