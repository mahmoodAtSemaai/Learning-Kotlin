package com.webkul.mobikul.odoo.model.payments

import android.graphics.drawable.Drawable

data class PaymentStatusDescription(
    val activityToolbarText: String,
    val buttonText: String,
    val headingText: String,
    val descriptionText: String,
    val drawable: Drawable?,
    val icon: Drawable?
)
