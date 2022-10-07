package com.webkul.mobikul.odoo.domain.enums

enum class UpdateAddressValidation(val value: Int) {
    EMPTY_DISPLAY_NAME(1),
    NULL_ADDRESS(2)
}

class UpdateAddressException(private val validationType: String) : Exception(validationType)
