package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PaymentStatusRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.PaymentStatusRepository
import javax.inject.Inject

class PaymentStatusRepositoryImpl @Inject constructor(
        private val remoteDataSource: PaymentStatusRemoteDataSource
) : PaymentStatusRepository {
    override suspend fun getPaymentTransactionStatus(orderId: Int): Resource<PaymentStatusEntity> {
        return remoteDataSource.getPaymentTransactionStatus(orderId)
    }

}