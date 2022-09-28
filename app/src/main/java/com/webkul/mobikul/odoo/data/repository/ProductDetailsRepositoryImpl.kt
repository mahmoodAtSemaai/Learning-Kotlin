package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ProductDetailsRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ProductDetailsRepository
import javax.inject.Inject

class ProductDetailsRepositoryImpl @Inject constructor(
        private val productDetailsRemoteDataSource: ProductDetailsRemoteDataSource
) : ProductDetailsRepository {
    override suspend fun get(templateId: Int): Resource<ProductEntity> {
        return productDetailsRemoteDataSource.get(templateId)
    }
}