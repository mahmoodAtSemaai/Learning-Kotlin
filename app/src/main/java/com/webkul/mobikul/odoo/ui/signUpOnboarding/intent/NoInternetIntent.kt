package com.webkul.mobikul.odoo.ui.signUpOnboarding.intent

import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class NoInternetIntent : IIntent {
    object Refresh : NoInternetIntent()
}