package com.webkul.mobikul.odoo.domain.usecase.network

import com.webkul.mobikul.odoo.core.utils.NetworkManager
import javax.inject.Inject

class IsNetworkUseCase @Inject constructor(
        private val networkManager: NetworkManager
) {

    operator fun invoke(): Boolean {
        return networkManager.isNetworkAvailable
    }

}