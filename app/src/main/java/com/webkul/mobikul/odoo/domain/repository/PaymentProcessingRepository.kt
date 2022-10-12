package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.data.entity.PaymentTransactionEntity
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest

interface PaymentProcessingRepository {

    suspend fun createPayment(createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest): Resource<PaymentTransactionEntity>
}