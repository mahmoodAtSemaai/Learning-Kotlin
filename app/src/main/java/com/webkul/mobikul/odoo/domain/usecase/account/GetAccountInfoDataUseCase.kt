package com.webkul.mobikul.odoo.domain.usecase.account

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.data.entity.AccountInfoEntity
import javax.inject.Inject

class GetAccountInfoDataUseCase @Inject constructor(private val appPreferences: AppPreferences) {
    operator fun invoke(): AccountInfoEntity {
        return AccountInfoEntity(
            name = appPreferences.customerName.toString(),
            phoneNumber = appPreferences.customerPhoneNumber.toString(),
            groupName = appPreferences.groupName,
            customerGroupName = appPreferences.customerGroupName,
            userName = appPreferences.userName
        )
    }
}