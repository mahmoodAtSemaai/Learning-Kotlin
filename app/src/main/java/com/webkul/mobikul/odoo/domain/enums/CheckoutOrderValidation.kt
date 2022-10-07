package com.webkul.mobikul.odoo.domain.enums

enum class CheckoutOrderValidation(val value: Int) {
    INVALID_SHIPPING_ADDRESS(1),
    INVALID_PAYMENT_METHOD(2),
    INVALID_SHIPPING_METHOD(3),
    INVALID_ORDER_DETAILS(4)
}

class CheckoutOrderValidationException(private val validationType: String) : Exception(validationType)