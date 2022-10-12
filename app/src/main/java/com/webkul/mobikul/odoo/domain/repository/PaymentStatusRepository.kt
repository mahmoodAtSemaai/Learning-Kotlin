package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity


interface PaymentStatusRepository {
    suspend fun getPaymentTransactionStatus(orderId: Int) : Resource<PaymentStatusEntity>
}