package com.webkul.mobikul.odoo.ui.checkout.order

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod

sealed class OrderIntent : IIntent {
       data class SetBundleDataAndInitialLayout(val bundle : Bundle?) : OrderIntent()
       data class FetchOrderDetails(val orderID: Int, val pointsRedeem: Boolean) : OrderIntent()
       data class FetchTransactionStatus(val orderID: Int) : OrderIntent()
       object CopyToClipBoard : OrderIntent()
}
