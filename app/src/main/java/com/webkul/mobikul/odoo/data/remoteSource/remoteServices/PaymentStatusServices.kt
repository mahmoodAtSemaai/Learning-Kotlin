package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.payments.PaymentStatusResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PaymentStatusServices {

    @GET(ApiInterface.MOBIKUL_GET_PAYMENTS_TRANSACTIONS)
    suspend fun getPaymentTransactionStatus(@Query(ApiInterface.MOBIKUL_ORDER_ID) orderId: Int): PaymentStatusResponse
}