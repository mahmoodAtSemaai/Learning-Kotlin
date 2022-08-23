package com.webkul.mobikul.odoo.ui.signUpAuth.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class SignUpAuthEffect : IEffect {
    object NavigateToLogin : SignUpAuthEffect()
    data class NavigateToSignUpAuthOptions(val phoneNumber : String) : SignUpAuthEffect()
}
