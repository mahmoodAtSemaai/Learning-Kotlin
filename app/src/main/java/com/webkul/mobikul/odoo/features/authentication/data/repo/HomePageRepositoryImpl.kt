package com.webkul.mobikul.odoo.features.authentication.data.repo

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.remoteSource.HomePageRemoteDataSource
import com.webkul.mobikul.odoo.features.authentication.domain.repo.HomePageRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import javax.inject.Inject

class HomePageRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomePageRemoteDataSource,
    private val appPreferences: AppPreferences
) : HomePageRepository {

    override suspend fun getHomePageData(): Resource<HomePageResponse> {
        val result =  remoteDataSource.getHomePageData()
        when(result){
            is Resource.Success -> {
                appPreferences.newCartCount = result.value.newCartCount
            }
        }
        return result
    }

}