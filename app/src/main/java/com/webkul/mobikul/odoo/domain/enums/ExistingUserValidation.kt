package com.webkul.mobikul.odoo.domain.enums

enum class ExistingUserValidation(val value: Int) {
    EXISTING_USER(1)
}

class ExistingUserValidationException(private val validationType: String) : Exception(validationType)