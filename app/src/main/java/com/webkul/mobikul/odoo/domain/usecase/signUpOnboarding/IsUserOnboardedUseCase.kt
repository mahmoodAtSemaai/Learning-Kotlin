package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import javax.inject.Inject

class IsUserOnboardedUseCase  @Inject constructor(
    private val appPreferences: AppPreferences
)  {
    operator fun invoke(): Boolean {
        return appPreferences.isUserOnboarded
    }
}