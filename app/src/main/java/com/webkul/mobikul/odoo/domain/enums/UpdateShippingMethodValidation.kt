package com.webkul.mobikul.odoo.domain.enums

enum class UpdateShippingMethodValidation(val value: Int) {
    NULL_SHIPPING_METHOD(0)
}

class UpdateShippingMethodException(private val validationType: String) : Exception(validationType)
