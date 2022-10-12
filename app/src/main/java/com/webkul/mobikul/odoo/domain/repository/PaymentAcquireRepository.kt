package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodProviderCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity

interface PaymentAcquireRepository {
    suspend fun getPaymentAcquirers(companyId: Int): Resource<PaymentAcquireCheckoutEntity>
    suspend fun getPaymentAcquirerMethods(acquirerId: Int): Resource<PaymentAcquirerMethodCheckoutEntity>
    suspend fun getPaymentAcquirerMethodProviders(
        acquirerId: Int,
        paymentMethodId: Int
    ): Resource<PaymentAcquirerMethodProviderCheckoutEntity>
}