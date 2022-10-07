package com.webkul.mobikul.odoo.ui.checkout.paymentstatus

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.ui.checkout.shippingmethod.ShippingMethodIntent

sealed class PaymentStatusIntent: IIntent{
    object ContinueShopping : PaymentStatusIntent()
    data class SetBundleData(val bundle: Bundle?) : PaymentStatusIntent()
    object SetInitialLayout: PaymentStatusIntent()

}