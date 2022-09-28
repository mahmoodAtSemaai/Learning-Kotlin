package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.SellerRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.SellerRepository
import javax.inject.Inject

class SellerRepositoryImpl @Inject constructor(
        private val sellerRemoteDataSource: SellerRemoteDataSource
) : SellerRepository {
    override suspend fun get(sellerId: Int): Resource<SellerDetailsEntity> {
        return sellerRemoteDataSource.get(sellerId)
    }
}