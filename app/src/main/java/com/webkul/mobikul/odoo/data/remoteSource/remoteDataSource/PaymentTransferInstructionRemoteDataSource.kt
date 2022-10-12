package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.PaymentTransferInstructionEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.PaymentTransferInstructionServices
import javax.inject.Inject

class PaymentTransferInstructionRemoteDataSource @Inject constructor(
        private val apiService: PaymentTransferInstructionServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getTransferInstruction(bankId: Int) = safeApiCall(PaymentTransferInstructionEntity::class.java){
        apiService.getTransferInstruction(bankId = bankId)
    }
}