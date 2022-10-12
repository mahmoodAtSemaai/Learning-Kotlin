package com.webkul.mobikul.odoo.domain.usecase.session

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AppConfigEntity
import com.webkul.mobikul.odoo.data.entity.UserEntity
import javax.inject.Inject

//TODO--> Move this use case in Local repo with localRepo migration
class IsValidSessionUseCase @Inject constructor() {

    operator fun invoke(appConfigEntity: AppConfigEntity?, userEntity: UserEntity?): Resource<Boolean> {
        val isUserLoggedIn = userEntity?.isUserLoggedIn ?: false
        val isAllowedGuestCheckout = appConfigEntity?.isAllowGuestCheckout ?: false
        val isUserOnboarded = userEntity?.isUserOnboarded ?: false

        return if (isUserLoggedIn.not() && isAllowedGuestCheckout.not()) {
            Resource.Failure(FailureStatus.ACCESS_DENIED, null, "")
        } else {
            Resource.Success(true)
        }
    }
}