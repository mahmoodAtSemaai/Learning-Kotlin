package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.PaymentStatusEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentStatusServices
import javax.inject.Inject

class PaymentStatusRemoteDataSource @Inject constructor(
        private val apiService: PaymentStatusServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences){

    suspend fun getPaymentTransactionStatus(orderId: Int) = safeApiCall(PaymentStatusEntity::class.java){
        apiService.getPaymentTransactionStatus(orderId = orderId)
    }
}
