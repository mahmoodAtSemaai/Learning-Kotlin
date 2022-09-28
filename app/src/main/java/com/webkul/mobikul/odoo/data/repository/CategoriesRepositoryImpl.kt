package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductCategoriesEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CategoriesRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
        private val categoriesRemoteDataSource: CategoriesRemoteDataSource
) : CategoriesRepository {
    override suspend fun get(): Resource<ProductCategoriesEntity> {
        return categoriesRemoteDataSource.get()
    }
}