package com.webkul.mobikul.odoo.features.authentication.data.repo

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.SplashRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.domain.repo.SplashPageRepository
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import javax.inject.Inject

class SplashPageRepositoryImpl @Inject constructor(
    private val remoteDataSource: SplashRemoteDataSource
) : SplashPageRepository {

    override suspend fun getSplashPageData(): Resource<SplashScreenResponse> {
        return remoteDataSource.getSplashPageData()
    }
}