package com.webkul.mobikul.odoo.ui.splash

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class SplashIntent : IIntent {
    data class CheckIntent(val intent: Intent) : SplashIntent()
    object StartInitUserAnalytics : SplashIntent()
    object StartInit : SplashIntent()
    object CheckUserLoggedIn : SplashIntent()
    object CheckFirstTimeUser : SplashIntent()
    object DeeplinkSplashPageData : SplashIntent()
    object CreateChatChannel : SplashIntent()
}
