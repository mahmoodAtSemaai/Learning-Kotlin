package com.webkul.mobikul.odoo.model.payments

data class OrderPaymentData(
    val paymentMethod: String,
    val totalPriceValue: String,
    val cartItemCount: String,
    val semaaiPointsRedeemedValue: String,
    var shippingMethodCostValue: String,
    val couponsUsedValue: String,
    var netPriceValue: String
) {


}