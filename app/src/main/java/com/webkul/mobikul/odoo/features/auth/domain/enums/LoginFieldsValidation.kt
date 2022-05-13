package com.webkul.mobikul.odoo.features.auth.domain.enums

enum class LoginFieldsValidation(val value: Int) {
    EMPTY_EMAIL(1),
   // INVALID_EMAIL(2),
    EMPTY_PASSWORD(3),
    INVALID_PASSWORD(4),
   // INVALID_LOGIN_DETAILS(5)
}

class LogInValidationException(private val validationType: String) : Exception(validationType)