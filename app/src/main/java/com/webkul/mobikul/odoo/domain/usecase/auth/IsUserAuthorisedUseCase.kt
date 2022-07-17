package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import javax.inject.Inject

class IsUserAuthorisedUseCase @Inject constructor(
        private val appPreferences: AppPreferences
) {

    operator fun invoke(): Boolean {
        return appPreferences.isLoggedIn or appPreferences.isAllowedGuestCheckout
    }

}