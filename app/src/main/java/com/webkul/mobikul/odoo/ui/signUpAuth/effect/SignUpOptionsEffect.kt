package com.webkul.mobikul.odoo.ui.signUpAuth.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class SignUpOptionsEffect : IEffect {
    data class NavigateToOTPSignUp(val phoneNumber: String) : SignUpOptionsEffect()
    object NavigateToPhoneNumberValidation : SignUpOptionsEffect()
}
