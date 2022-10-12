package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SplashEntity
import com.webkul.mobikul.odoo.data.localSource.localDataSource.SplashLocalDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SplashRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SplashDataRepository
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import javax.inject.Inject

class SplashDataRepositoryImpl @Inject constructor(
    private val remotePersistenceSource: SplashRemoteDataSource,
    private val localPersistenceSource: SplashLocalDataSource,
) : SplashDataRepository,
    BaseRepository<SplashEntity, Any, SplashScreenResponse, SplashScreenResponse>() {

    override val entityParser
        get() = ModelEntityParser(SplashEntity::class.java, SplashScreenResponse::class.java)

    override var dataSource: IDataSource<SplashScreenResponse, Any, SplashScreenResponse> =
        remotePersistenceSource

    override suspend fun get(): Resource<SplashEntity> {
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