package com.webkul.mobikul.odoo.features.authentication.data.repo

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import javax.inject.Inject

class HomePageRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomePageRemoteDataSource
) : HomePageRepository {

    override suspend fun getHomePageData(): Resource<HomePageResponse> {
        return remoteDataSource.getHomePageData()
    }

}