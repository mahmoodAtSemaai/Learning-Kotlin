package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodProviderCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentTransactionEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentAcquireServices
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentProcessingServices
import com.webkul.mobikul.odoo.model.payments.CreateVirtualAccountPaymentRequest
import javax.inject.Inject

class PaymentProcessingRemoteDataSource  @Inject constructor(
    private val apiService: PaymentProcessingServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun createPayment(createVirtualAccountPaymentRequest: CreateVirtualAccountPaymentRequest) = safeApiCall(PaymentTransactionEntity::class.java) {
        apiService.createPayment(createVirtualAccountPaymentRequest.toString())
    }
}