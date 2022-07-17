package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import javax.inject.Inject

class IsUserApprovedUseCase @Inject constructor(
        private val appPreferences: AppPreferences
) {

    operator fun invoke(): Boolean {
        return appPreferences.isUserApproved
    }

}