package com.webkul.mobikul.odoo.features.auth.domain.usecase

import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.features.auth.domain.enums.SignUpValidationException
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.features.auth.presentation.SignUpData
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {

    @Throws(SignUpValidationException::class)
    operator fun invoke(signUpData: SignUpData): Flow<Resource<SignUpResponse>> = flow {

        emit(Resource.Loading)

        if (isValidLogin(signUpData)) {

            //  val signUpRequest = SignUpRequest()

            //   val result = signUpRepository.signUp(signUpData)
            //  emit(result)
        }

    }.flowOn(Dispatchers.IO)


    private fun isValidLogin(signUpData: SignUpData): Boolean {

        if (signUpData.phoneNumber.isEmpty()) throw SignUpValidationException(
            SignUpFieldsValidation.EMPTY_PHONE_NO.value.toString()
        )

        if (signUpData.password.isEmpty() ) throw SignUpValidationException(
            SignUpFieldsValidation.EMPTY_PASSWORD.value.toString()
        )

        if (signUpData.password.length  < BuildConfig.MIN_PASSWORD_LENGTH) throw SignUpValidationException(
            SignUpFieldsValidation.INVALID_PASSWORD.value.toString()
        )

        if (signUpData.confirmPassword  !=  signUpData.password) throw SignUpValidationException(
            SignUpFieldsValidation.INVALID_PASSWORD.value.toString()
        )


        return true
    }


}