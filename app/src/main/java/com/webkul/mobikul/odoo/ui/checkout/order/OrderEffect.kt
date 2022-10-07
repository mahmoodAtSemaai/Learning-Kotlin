package com.webkul.mobikul.odoo.ui.checkout.order

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class OrderEffect : IEffect {
    object CopyToClipBoard : OrderEffect()
}
