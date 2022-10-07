package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.payments.PaymentStatusResponse
import com.webkul.mobikul.odoo.model.payments.PaymentTransactionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentProcessingServices {

    @POST(ApiInterface.MOBIKUL_CREATE_PAYMENTS_TRANSACTIONS)
    suspend fun createPayment(@Body payment: String): PaymentTransactionResponse
}