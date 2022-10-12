package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.domain.enums.VerifyPhoneNumberException
import com.webkul.mobikul.odoo.domain.enums.VerifyPhoneNumberValidation
import com.webkul.mobikul.odoo.domain.repository.PhoneNumberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContinuePhoneNumberUseCase @Inject constructor(
        private val phoneNumberRepository: PhoneNumberRepository
) {

    @Throws(VerifyPhoneNumberException::class)
    operator fun invoke(phoneNumber: String): Flow<Resource<UserCreateDateEntity>> = flow {
        emit(Resource.Loading)
        val result = phoneNumberRepository.validatePhoneNumber(phoneNumber)
        when (result) {
            is Resource.Failure -> {
                if (result.failureStatus == FailureStatus.RESOURCE_NOT_FOUND)
                    throw VerifyPhoneNumberException(VerifyPhoneNumberValidation.INVALID_PHONE_NUMBER.value.toString())
            }
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

}