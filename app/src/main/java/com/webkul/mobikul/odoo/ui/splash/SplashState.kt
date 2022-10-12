package com.webkul.mobikul.odoo.ui.splash

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus

sealed class SplashState : IState {
    object Idle : SplashState()
    object Loading : SplashState()
    object NotificationIntent : SplashState()
    object SplashIntent : SplashState()
    object UserUnAuthenticated : SplashState()
    object UserLoggedIn : SplashState()
    object InitiatedUserAnalytics : SplashState()
    object ConfigurationReceived: SplashState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : SplashState()
}
