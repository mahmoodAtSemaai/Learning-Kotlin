package com.webkul.mobikul.odoo.domain.enums

enum class VerifyOTPValidation (val value: Int) {
    INVALID_OTP(1)
}

class VerifyOTPException(private val validationType: String) : Exception(validationType)
