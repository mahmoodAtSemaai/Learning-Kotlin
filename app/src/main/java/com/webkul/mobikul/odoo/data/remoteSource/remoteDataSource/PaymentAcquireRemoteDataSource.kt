package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.PaymentAcquireCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodProviderCheckoutEntity
import com.webkul.mobikul.odoo.data.entity.PaymentAcquirerMethodCheckoutEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentAcquireServices
import javax.inject.Inject

class PaymentAcquireRemoteDataSource  @Inject constructor(
    private val apiService: PaymentAcquireServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getPaymentAcquirers(companyId : Int) = safeApiCall(PaymentAcquireCheckoutEntity::class.java) {
        apiService.getPaymentAcquirers(companyId)
    }

    suspend fun getPaymentAcquirerMethods(acquirerId : Int) = safeApiCall(PaymentAcquirerMethodCheckoutEntity::class.java) {
        apiService.getPaymentAcquirerMethods(acquirerId)
    }

    suspend fun getPaymentAcquirerMethodProviders(acquirerId : Int, paymentMethodId : Int) =
        safeApiCall(PaymentAcquirerMethodProviderCheckoutEntity::class.java) {
            apiService.getPaymentAcquirerMethodProviders(acquirerId, paymentMethodId)
        }

}