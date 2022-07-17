package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.SaveData
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.HomeRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.HomeDataRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import javax.inject.Inject

class HomeDataRepositoryImpl @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource,
    private val saveData: SaveData
) : HomeDataRepository {

    override suspend fun getHomePageData(): Resource<HomePageResponse> {
//TODO->Handle homepage Response with home revamp
        val result = remoteDataSource.getHomePageData()
        when (result) {
            is Resource.Success -> {
                if (result.value.isSuccess) saveData.saveHomepageResponse(result.value)
            }
        }

        return result
    }
}