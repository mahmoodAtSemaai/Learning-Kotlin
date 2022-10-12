package com.webkul.mobikul.odoo.ui.checkout.paymentprocessing

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class PaymentProcessingIntent  : IIntent {
    data class SetBundleData(val bundle: Bundle?) : PaymentProcessingIntent()
    data class ShowErrorDialog(val title: String?, val message: String?) : PaymentProcessingIntent()
}
