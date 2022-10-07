package com.webkul.mobikul.odoo.ui.onboarding.state

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.OnboardingData

sealed class OnboardingState : IState {
    object Idle : OnboardingState()
    data class Onboarding(val onboardingData: MutableList<OnboardingData>) : OnboardingState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : OnboardingState()
    data class CountDownTimer(val timeLeft: Int) : OnboardingState()
    object CountDownTimerFinish : OnboardingState()
    object NextSlide : OnboardingState()
    object SkipSlides : OnboardingState()
}
