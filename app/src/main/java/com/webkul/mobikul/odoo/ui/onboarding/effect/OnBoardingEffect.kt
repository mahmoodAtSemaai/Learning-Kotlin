package com.webkul.mobikul.odoo.ui.onboarding.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class OnBoardingEffect : IEffect{
    object NavigateToSignInSignUpScreen : OnBoardingEffect()
}
