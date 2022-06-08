package com.webkul.mobikul.odoo.features.authentication.domain.usecase

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberException
import com.webkul.mobikul.odoo.features.authentication.domain.enums.VerifyPhoneNumberValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VerifyPhoneNumberUseCase @Inject constructor() {

    private val INDONESIAN_NUMBER_CODE = "08"

    @Throws(VerifyPhoneNumberException::class)
    operator fun invoke(phoneNumber: String): Flow<Resource<Unit>> = flow {

        if (phoneNumber.isBlank()) {
            val result = Resource.Default
            emit(result)
        } else if ((isValidPhoneNumber(phoneNumber) && isValidPhoneNumberFormat(phoneNumber))) {
            val result = Resource.Success(Unit)
            emit(result)
        } else {
            if (!isValidPhoneNumberFormat(phoneNumber))
                throw VerifyPhoneNumberException(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER_FORMAT.value.toString())
            if (!isValidPhoneNumber(phoneNumber))
                throw VerifyPhoneNumberException(VerifyPhoneNumberValidation.INCORRECT_PHONE_NUMBER.value.toString())
        }

    }.flowOn(Dispatchers.IO)

    private fun isValidPhoneNumberFormat(phoneNumber: String): Boolean {
        return phoneNumber.isNotBlank() && phoneNumber.startsWith(INDONESIAN_NUMBER_CODE, true)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.isNotBlank() &&
                phoneNumber.startsWith(INDONESIAN_NUMBER_CODE, true) &&
                phoneNumber.length in 10..13
    }
}