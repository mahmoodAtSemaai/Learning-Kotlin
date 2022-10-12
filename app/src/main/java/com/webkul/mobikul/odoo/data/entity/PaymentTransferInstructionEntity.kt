package com.webkul.mobikul.odoo.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction

data class PaymentTransferInstructionEntity(
        @SerializedName("status")
        @Expose
        val status: String,
        @SerializedName("result")
        @Expose
        val list: List<TransferInstructionEntity>
)
