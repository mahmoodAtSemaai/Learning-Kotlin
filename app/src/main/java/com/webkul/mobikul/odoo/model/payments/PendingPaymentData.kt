package com.webkul.mobikul.odoo.model.payments

import android.os.Parcelable
import com.webkul.mobikul.odoo.helper.CalendarUtil
import com.webkul.mobikul.odoo.model.checkout.Bank
import kotlinx.android.parcel.Parcelize
import java.text.NumberFormat

@Parcelize
data class PendingPaymentData(
    val expireDate: String,
    val expireTime: String,
    val paymentAmount: String,
    val bank: Bank,
    val orderId: Int,
    val isCheckout: Boolean
) : Parcelable {

    companion object {
        const val CURRENCY = "Rp"
    }

    fun getFormattedPaymentAmount(): String {
        return if(paymentAmount.startsWith(CURRENCY)){
            paymentAmount
        }else {
            CURRENCY.plus(
                NumberFormat.getCurrencyInstance()
                    .format(
                        paymentAmount.toDouble()
                    ).substring(1)
            )
        }
    }

    fun getPaymentExpiryDate(): String {
        if (expireDate == null)
            return ""
        return CalendarUtil.getDateInBahasa(expireDate)
    }


}