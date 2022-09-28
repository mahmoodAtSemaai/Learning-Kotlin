package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CategoryProductRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.CategoryProductRepository
import javax.inject.Inject

class CategoryProductRepositoryImpl @Inject constructor(
        private val categoryProductRemoteDataSource: CategoryProductRemoteDataSource
) : CategoryProductRepository {
    override suspend fun get(categoryId: Int, globalProductsEnabled : Boolean, offset: Int, limit: Int): Resource<ProductListEntity> {
        return categoryProductRemoteDataSource.get(categoryId,globalProductsEnabled, offset, limit)
    }
}