package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CountryStateRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CountryStateRepository
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import javax.inject.Inject

class CountryStateRepositoryImpl @Inject constructor(
    remoteDataSource: CountryStateRemoteDataSource
) : CountryStateRepository, BaseRepository<CountryEntity, Any, Any, CountryStateData>() {

    override val entityParser: ModelEntityParser<CountryEntity, CountryStateData>
        get() = ModelEntityParser(CountryEntity::class.java, CountryStateData::class.java)

    override var dataSource: IDataSource<CountryStateData, Any, Any> = remoteDataSource

    override suspend fun get(): Resource<CountryEntity> {
        return super<BaseRepository>.get()
    }
}