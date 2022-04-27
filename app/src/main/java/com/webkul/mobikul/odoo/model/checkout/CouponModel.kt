package com.webkul.mobikul.odoo.model.checkout

data class CouponModel(
    val couponsName: String,
    val minimumTransactionText: String,
    val minimumTransactionValue: Int,
    val expiredDatetext: String,
    val expireddateValue: String
) {
    var isSelected = false
}