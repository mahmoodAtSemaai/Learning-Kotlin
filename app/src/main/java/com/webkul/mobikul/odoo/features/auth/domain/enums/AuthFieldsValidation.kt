package com.webkul.mobikul.odoo.features.auth.domain.enums

enum class AuthFieldsValidation(val value: Int) {
    EMPTY_EMAIL(1),
    INVALID_EMAIL(2),
    EMPTY_PASSWORD(3),
    INVALID_PASSWORD(2),
}

class LogInValidationException(private val validationType: String) : Exception(validationType)