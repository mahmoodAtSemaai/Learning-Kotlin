package com.webkul.mobikul.odoo.ui.signUpOnboarding.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class UserDetailsEffect : IEffect {
    object HideInvalidReferralCode : UserDetailsEffect()
}