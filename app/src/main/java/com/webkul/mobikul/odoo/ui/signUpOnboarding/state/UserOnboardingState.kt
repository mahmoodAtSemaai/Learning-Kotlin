package com.webkul.mobikul.odoo.ui.signUpOnboarding.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse

sealed class UserOnboardingState : IState {
    object Idle : UserOnboardingState()
    object Loading : UserOnboardingState()
    object SetToolbar : UserOnboardingState()
    object ExistingUser : UserOnboardingState()
    object GetUserOnboardingStage : UserOnboardingState()
    object GetHomePageData : UserOnboardingState()
    object OnboardingStage : UserOnboardingState()
    object GetIncompleteStage : UserOnboardingState()
    object LaunchNextStage : UserOnboardingState()
    data class Splash(val splashScreenResponse: SplashScreenResponse) : UserOnboardingState()
    data class UserStage(val stageId: List<Int>) : UserOnboardingState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : UserOnboardingState()
}