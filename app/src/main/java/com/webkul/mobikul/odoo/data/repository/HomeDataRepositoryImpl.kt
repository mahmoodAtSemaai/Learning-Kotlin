package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.localSource.localDataSource.HomeLocalDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.HomeRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.HomeDataRepository
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import javax.inject.Inject

class HomeDataRepositoryImpl @Inject constructor(
    private val remotePersistenceSource: HomeRemoteDataSource,
    private val localPersistenceSource: HomeLocalDataSource
) : HomeDataRepository,
    BaseRepository<HomePageResponse, Any, HomePageResponse, HomePageResponse>() {

    //TODO->Handle homepage Response with home revamp
    override val entityParser
        get() = ModelEntityParser(HomePageResponse::class.java, HomePageResponse::class.java)

    override var dataSource: IDataSource<HomePageResponse, Any, HomePageResponse> =
        remotePersistenceSource

    override suspend fun get(): Resource<HomePageResponse> {
        dataSource = remotePersistenceSource
        return when (val result = super<BaseRepository>.get()) {
            is Resource.Success -> {
                dataSource = localPersistenceSource
                super<BaseRepository>.update(entityParser.fromObject(result.value))
                result
            }
            else -> result
        }
    }
}