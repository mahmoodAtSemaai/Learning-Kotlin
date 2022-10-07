package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.data.entity.PaymentTransactionEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PaymentProcessingRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PaymentStatusRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.PaymentProcessingRepository
import com.webkul.mobikul.odoo.domain.repository.PaymentStatusRepository
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import javax.inject.Inject

class PaymentProcessingRepositoryImpl @Inject constructor(
        private val remoteDataSource: PaymentProcessingRemoteDataSource
) : PaymentProcessingRepository {
    override suspend fun createPayment(createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest):
            Resource<PaymentTransactionEntity> {
        return remoteDataSource.createPayment(createVirtualAccountPaymentRequest)
    }

}