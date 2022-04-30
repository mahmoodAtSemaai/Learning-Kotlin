package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.webkul.mobikul.odoo.model.checkout.Bank
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat

@Parcelize
data class PaymentDetails(
    @SerializedName("checkout_url")
    @Expose
    val checkoutUrl: String,
    @SerializedName("expire_time")
    @Expose
    val expireTime: String,
    @SerializedName("expire_date")
    @Expose
    val expireDate: String,
    @SerializedName("payment_amount")
    @Expose
    val paymentAmount: String,
    @SerializedName("bank")
    @Expose
    val bank: Bank
) : Parcelable {

    companion object {
        const val CURRENCY = "Rp"
    }

    fun getAccountNumber(): String {
        return bank.getAccountNumber()
    }

    fun getFormattedPaymentAmount(): String {
        return CURRENCY.plus(
            NumberFormat.getCurrencyInstance()
                .format(
                    paymentAmount.toDouble()
                ).substring(1)
        )
    }

}