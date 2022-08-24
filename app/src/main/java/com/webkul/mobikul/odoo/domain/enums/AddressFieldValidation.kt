package com.webkul.mobikul.odoo.domain.enums

enum class AddressFieldValidation(val value: Int) {
    UNAVAILABLE_PROVINCE(1),
    NO_RESULTS_FOUND(2)
}

class AddressFieldValidationException(private val validationType: String) : Exception(validationType)