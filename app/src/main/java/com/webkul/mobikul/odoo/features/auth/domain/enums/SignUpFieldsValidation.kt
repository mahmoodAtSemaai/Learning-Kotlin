package com.webkul.mobikul.odoo.features.auth.domain.enums

enum class SignUpFieldsValidation(val value: Int) {
    EMPTY_PHONE_NO(1),
    EMPTY_NAME(2),
    INVALID_PHONE_NO(3),
    EMPTY_PASSWORD(4),
    INVALID_PASSWORD(5),
    UNEQUAL_PASS_AND_CONFIRM_PASS(6),
    EMPTY_TERMS_CONDITIONS(7),
    EMPTY_PROFILE_URL(8),
    EMPTY_COUNTRY(9),

}

class SignUpValidationException(private val validationType: String) : Exception(validationType)