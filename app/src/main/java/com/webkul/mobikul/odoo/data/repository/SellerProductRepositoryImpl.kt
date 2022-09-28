package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SellerProductsRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SellerProductRepository
import javax.inject.Inject

class SellerProductRepositoryImpl @Inject constructor(
        private val sellerProductsRemoteDataSource: SellerProductsRemoteDataSource
) : SellerProductRepository {
    override suspend fun get(sellerId: Int): Resource<ProductListEntity> {
        return sellerProductsRemoteDataSource.get(sellerId)
    }
}