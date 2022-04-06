package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class CreateVirtualAccountPaymentRequest(
    @SerializedName("currency")
    @Expose
    val currency: String,
    @SerializedName("method_provider_id")
    @Expose
    val methodProviderId: String,
    @SerializedName("order_id")
    @Expose
    val orderId: String,
    @SerializedName("transaction_id")
    @Expose
    val transactionId: String
) : Parcelable {

    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("currency", currency)
        jsonObject.put("method_provider_id", methodProviderId)
        jsonObject.put("order_id", orderId)
        jsonObject.put("transaction_id", transactionId)
        return jsonObject.toString()
    }
}