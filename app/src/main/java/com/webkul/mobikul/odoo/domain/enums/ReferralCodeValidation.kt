package com.webkul.mobikul.odoo.domain.enums

enum class ReferralCodeValidation (val value: Int) {
    INVALID_REFERRAL_CODE(1)
}

class ReferralCodeValidationException(private val validationType: String) : Exception(validationType)