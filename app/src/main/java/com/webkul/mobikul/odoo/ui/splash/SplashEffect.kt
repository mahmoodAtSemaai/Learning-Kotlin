package com.webkul.mobikul.odoo.ui.splash

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class SplashEffect : IEffect {
    object NavigateToOnBoardingActivity : SplashEffect()
    object NavigateToSignInSignUpActivity : SplashEffect()
    object NavigateToUserApprovalActivity : SplashEffect()
    data class NavigateToHomeScreen(val homePageResponse: HomePageResponse) : SplashEffect()
    object ExecuteDeepLink : SplashEffect()
}
