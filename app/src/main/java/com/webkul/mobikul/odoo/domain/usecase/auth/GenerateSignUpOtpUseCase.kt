package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.OtpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GenerateSignUpOtpUseCase @Inject constructor(
    private val otpRepository: OtpRepository
) {
    operator fun invoke(phoneNumber: String): Flow<Resource<Any>> = flow {

        emit(Resource.Loading)
        val result = otpRepository.create(phoneNumber)
        emit(result)

    }.flowOn(Dispatchers.IO)
}