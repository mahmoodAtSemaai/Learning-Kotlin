package com.webkul.mobikul.odoo.model.payments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction

data class TransferInstructionResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("status_code")
    @Expose
    val statusCode: String,
    @SerializedName("result")
    @Expose
    val list: List<TransferInstruction>,
    @SerializedName("message")
    @Expose
    val message: String
)