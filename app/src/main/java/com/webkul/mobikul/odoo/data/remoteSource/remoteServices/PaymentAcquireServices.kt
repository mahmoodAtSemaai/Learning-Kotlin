package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface.*
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethodProviderResponse
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethodResponse
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirersResponse
import com.webkul.mobikul.odoo.model.payments.PaymentsAPIConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PaymentAcquireServices {

    @GET(MOBIKUL_PAYMENTS_ACQUIRERS)
    suspend fun getPaymentAcquirers(@Query(PaymentsAPIConstants.COMPANY_ID) companyId : Int) : PaymentAcquirersResponse

    //TODO: NEED TO WRITE STRING IN SUCH A WAY BECAUSE OF COMPILE TIME ERROR
    @GET("payment/acquirers/{" + PaymentsAPIConstants.ACQUIRER_ID + "}/methods")
    suspend fun getPaymentAcquirerMethods(@Path(PaymentsAPIConstants.ACQUIRER_ID) acquirerId : Int) : PaymentAcquirerMethodResponse

    //TODO: NEED TO WRITE STRING IN SUCH A WAY BECAUSE OF COMPILE TIME ERROR
    @GET("payment/acquirers/{" + PaymentsAPIConstants.PAYMENT_METHOD_ID + "}/methods/{" + PaymentsAPIConstants.ACQUIRER_ID + "}/providers")
    suspend fun getPaymentAcquirerMethodProviders(@Path(PaymentsAPIConstants.ACQUIRER_ID) acquirerId : Int,
                                                  @Path(PaymentsAPIConstants.PAYMENT_METHOD_ID) paymentMethodId : Int) : PaymentAcquirerMethodProviderResponse


}