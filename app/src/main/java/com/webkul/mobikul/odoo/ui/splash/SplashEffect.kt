package com.webkul.mobikul.odoo.ui.splash

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class SplashEffect : IEffect {
    object NavigateToOnBoardingActivity : SplashEffect()
    object NavigateToSignInSignUpActivity : SplashEffect()
    object NavigateToHomeScreen : SplashEffect()
    object ExecuteDeepLink : SplashEffect()
}
