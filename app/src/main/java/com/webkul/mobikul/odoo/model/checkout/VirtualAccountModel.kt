package com.webkul.mobikul.odoo.model.checkout

import android.graphics.drawable.Drawable

data class VirtualAccountModel(
    val logo: Drawable,
    val eWalletHeading: String,
    val eWalletDesciption: String,
    val isChecked: Boolean
)