package com.webkul.mobikul.odoo.ui.signUpAuth.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class SignUpOtpEffect : IEffect {
    object NavigateToUserOnboarding : SignUpOtpEffect()
}
