package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.data.request.AppConfigRequest
import com.webkul.mobikul.odoo.domain.repository.AppConfigRepository
import javax.inject.Inject


class SetIsFirstTimeUseCase @Inject constructor(
        private val appConfigRepository: AppConfigRepository
) {

    suspend operator fun invoke() {
        appConfigRepository.update(AppConfigRequest(isAppRunFirstTime = false))
    }

}