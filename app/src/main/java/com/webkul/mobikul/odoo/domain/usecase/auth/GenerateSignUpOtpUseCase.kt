package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.SignUpAuthRepository
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GenerateSignUpOtpUseCase  @Inject constructor(
    private val signUpAuthRepository: SignUpAuthRepository
) {
    operator fun invoke(phoneNumber:String): Flow<Resource<BaseOtpLoginResponse<Any>>> = flow {

        emit(Resource.Loading)
        val result = signUpAuthRepository.generateOTP(phoneNumber)
        emit(result)

    }.flowOn(Dispatchers.IO)
}