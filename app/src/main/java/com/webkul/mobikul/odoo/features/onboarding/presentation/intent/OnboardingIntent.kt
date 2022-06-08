package com.webkul.mobikul.odoo.features.onboarding.presentation.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class OnboardingIntent : IIntent{
    object GetOnboardingData : OnboardingIntent()
    object NextSlider : OnboardingIntent()
    object SkipSliders : OnboardingIntent()
    data class StartTimer(val time : Int) : OnboardingIntent()
    object SetIdle : OnboardingIntent()
}
