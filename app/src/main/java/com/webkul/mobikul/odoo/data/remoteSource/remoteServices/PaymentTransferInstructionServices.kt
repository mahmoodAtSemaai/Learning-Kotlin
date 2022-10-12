package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.payments.TransferInstructionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PaymentTransferInstructionServices {

    @GET(ApiInterface.MOBIKUL_GET_PAYMENTS_INSTRUCTIONS)
    suspend fun getTransferInstruction(@Path(ApiInterface.MOBIKUL_BANK_ID) bankId: Int): TransferInstructionResponse
}