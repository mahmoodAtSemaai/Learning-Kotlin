package com.webkul.mobikul.odoo.data.repository


import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.PaymentAcquireRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.PaymentAcquireRepository
import javax.inject.Inject

class PaymentAcquireRepositoryImpl @Inject constructor(
    private val remoteDataSource: PaymentAcquireRemoteDataSource
) : PaymentAcquireRepository {

    override suspend fun getPaymentAcquirers(companyId : Int): Resource<PaymentAcquireCheckoutEntity> {
        return remoteDataSource.getPaymentAcquirers(companyId)
    }

    override suspend fun getPaymentAcquirerMethods(acquirerId : Int): Resource<PaymentAcquirerMethodCheckoutEntity> {
        return remoteDataSource.getPaymentAcquirerMethods(acquirerId)
    }

    override suspend fun getPaymentAcquirerMethodProviders(acquirerId : Int, paymentMethodId : Int): Resource<PaymentAcquirerMethodProviderCheckoutEntity> {
        return remoteDataSource.getPaymentAcquirerMethodProviders(acquirerId, paymentMethodId)
    }

}