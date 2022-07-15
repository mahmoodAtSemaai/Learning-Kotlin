package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CountryStateRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CountryStateRepository
import javax.inject.Inject

class CountryStateRepositoryImpl @Inject constructor(
        private val remoteDataSource: CountryStateRemoteDataSource
) : CountryStateRepository {

    override suspend fun getCountryState(): Resource<CountryEntity> {
        return remoteDataSource.getCountryState()
    }

}