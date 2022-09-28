package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SearchRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
        private val searchRemoteDataSource: SearchRemoteDataSource
) : SearchRepository {
    override suspend fun get(searchQuery: String): Resource<ProductListEntity> {
        return searchRemoteDataSource.get(searchQuery)
    }
}