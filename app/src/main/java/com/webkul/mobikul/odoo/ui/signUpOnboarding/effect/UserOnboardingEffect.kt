package com.webkul.mobikul.odoo.ui.signUpOnboarding.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.model.home.HomePageResponse

sealed class UserOnboardingEffect : IEffect {

    object NavigateToCustomerGroup : UserOnboardingEffect()

    object NavigateToDetailsScreen : UserOnboardingEffect()

    data class NavigateToAddressScreen(val isAddressStagePending: Boolean) : UserOnboardingEffect()

    data class NavigateToHomeScreen(val homePageResponse: HomePageResponse) : UserOnboardingEffect()

    object RelaunchActivity : UserOnboardingEffect()
}
