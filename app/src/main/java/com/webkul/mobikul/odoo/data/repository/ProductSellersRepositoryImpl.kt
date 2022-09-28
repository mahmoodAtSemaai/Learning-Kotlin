package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ProductSellersRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ProductSellersRepository
import javax.inject.Inject

class ProductSellersRepositoryImpl @Inject constructor(
        private val productSellersRemoteDataSource: ProductSellersRemoteDataSource
) : ProductSellersRepository {
    override suspend fun get(globalProductID: Int, fromProductTemplate: Int): Resource<ProductListEntity> {
        return productSellersRemoteDataSource.get(globalProductID, fromProductTemplate)
    }
}