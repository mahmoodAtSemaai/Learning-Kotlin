package com.webkul.mobikul.odoo.domain.usecase.session

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.usecase.auth.IsUserAuthorisedUseCase
import javax.inject.Inject

//TODO--> Move this use case in Local repo with localRepo migration
class IsValidSessionUseCase @Inject constructor(
        private val isUserAuthorisedUseCase: IsUserAuthorisedUseCase
) {

    operator fun invoke(): Resource<Boolean> {
        val isUserAuthorised = isUserAuthorisedUseCase()
        return if (isUserAuthorised.not()) {
            Resource.Failure(FailureStatus.ACCESS_DENIED, null, "")
        } else {
            Resource.Success(true)
        }
    }
}