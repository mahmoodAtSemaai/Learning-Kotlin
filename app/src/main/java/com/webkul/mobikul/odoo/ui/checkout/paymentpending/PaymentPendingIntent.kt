package com.webkul.mobikul.odoo.ui.checkout.paymentpending

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.PendingPaymentEntity
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData

sealed class PaymentPendingIntent: IIntent {
    data class DisplayOrderDetails(val orderId: Int) : PaymentPendingIntent()
    data class CheckPaymentStatus(val orderId: Int) : PaymentPendingIntent()
    data class DisplayTransferInstruction(val pendingPaymentData: PendingPaymentEntity) : PaymentPendingIntent()
    data class SetBundleData(val bundle: Bundle?) : PaymentPendingIntent()
    object CopyToClipBoard : PaymentPendingIntent()
}