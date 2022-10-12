package com.webkul.mobikul.odoo.ui.signUpOnboarding.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class UserOnboardingIntent : IIntent {
    object SetToolbar : UserOnboardingIntent()
    object LaunchNextStage : UserOnboardingIntent()
    data class StageCompleted(val stageName : List<String>) : UserOnboardingIntent()
    object GetUserIdCustomerId : UserOnboardingIntent()
    object GetOnboardingStage : UserOnboardingIntent()
    object GetUserOnboardingStage : UserOnboardingIntent()
    object GetIncompleteStage : UserOnboardingIntent()
    object GetHomeData:UserOnboardingIntent()
    object Refresh : UserOnboardingIntent()
}
