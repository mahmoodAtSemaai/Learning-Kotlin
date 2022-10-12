package com.webkul.mobikul.odoo.ui.checkout.paymentstatus

import android.content.Intent
import android.view.View
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.DeliveryEntity
import com.webkul.mobikul.odoo.data.entity.OrderEntity
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.data.entity.OrderReviewEntity
import com.webkul.mobikul.odoo.model.payments.SelectedPaymentMethod
import com.webkul.mobikul.odoo.ui.checkout.address.UpdateAddressState

sealed class PaymentStatusEffect : IEffect {

}
