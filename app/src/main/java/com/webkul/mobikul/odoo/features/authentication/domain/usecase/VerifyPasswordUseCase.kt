package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPasswordException
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPasswordValidation
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyPasswordUseCase @Inject constructor() {

    @Throws(VerifyPasswordException::class)
    operator fun invoke(phoneNumber: String): Flow<Resource<BaseOtpLoginResponse<Any>>> = flow {

        if (phoneNumber.isNotBlank()) {
            val result = Resource.Default
            emit(result)
        } else {

            if (!isValidPassword(phoneNumber)) throw VerifyPhoneNumberException(
                VerifyPasswordValidation.INCORRECT_PASSWORD.value.toString()
            )
        }

    }.flowOn(Dispatchers.IO)

    private fun isValidPassword(phoneNumber: String): Boolean {
        return phoneNumber.isNotBlank()
    }

}