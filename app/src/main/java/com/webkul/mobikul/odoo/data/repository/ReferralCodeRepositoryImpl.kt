package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ReferralCodeRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ReferralCodeRepository
import javax.inject.Inject

class ReferralCodeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReferralCodeRemoteDataSource
) : ReferralCodeRepository {
    override suspend fun validateReferralCode(referralCode: String): Resource<Any> {
        return remoteDataSource.validateReferralCode(referralCode)
    }
}