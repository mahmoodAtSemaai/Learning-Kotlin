package com.webkul.mobikul.odoo.domain.usecase.auth

import android.content.Context
import com.webkul.mobikul.odoo.BuildConfig
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SignUpData
import com.webkul.mobikul.odoo.data.entity.SignUpEntity
import com.webkul.mobikul.odoo.domain.enums.SignUpFieldsValidation
import com.webkul.mobikul.odoo.domain.enums.SignUpValidationException
import com.webkul.mobikul.odoo.domain.repository.AuthRepository
import com.webkul.mobikul.odoo.model.request.SignUpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
        private val authRepository: AuthRepository,
        private val context: Context
) {

    @Throws(SignUpValidationException::class)
    operator fun invoke(signUpData: SignUpData): Flow<Resource<SignUpEntity>> = flow {

        emit(Resource.Loading)

        if (isValidSignUpData(signUpData)) {
            val signUpRequest = SignUpRequest(context, signUpData)
            val result = authRepository.signUp(signUpRequest)

            emit(result)

        }

    }.flowOn(Dispatchers.IO)


    private fun isValidSignUpData(signUpData: SignUpData): Boolean {

        if (signUpData.phoneNumber.isEmpty()) throw SignUpValidationException(
                SignUpFieldsValidation.EMPTY_PHONE_NO.value.toString()
        )

        if (signUpData.name.isEmpty()) throw SignUpValidationException(
                SignUpFieldsValidation.EMPTY_NAME.value.toString()
        )

        if (signUpData.password.isEmpty()) throw SignUpValidationException(
                SignUpFieldsValidation.EMPTY_PASSWORD.value.toString()
        )

        if (signUpData.password.length < BuildConfig.MIN_PASSWORD_LENGTH) throw SignUpValidationException(
                SignUpFieldsValidation.INVALID_PASSWORD.value.toString()
        )

        if (signUpData.confirmPassword != signUpData.password) throw SignUpValidationException(
                SignUpFieldsValidation.UNEQUAL_PASS_AND_CONFIRM_PASS.value.toString()
        )

        if (!signUpData.isTermAndCondition) throw SignUpValidationException(
                SignUpFieldsValidation.EMPTY_TERMS_CONDITIONS.value.toString()
        )

        if (signUpData.isSeller) {

            if (signUpData.profileURL.isEmpty()) throw SignUpValidationException(
                    SignUpFieldsValidation.EMPTY_PROFILE_URL.value.toString()
            )

            if (signUpData.country.isEmpty()) throw SignUpValidationException(
                    SignUpFieldsValidation.EMPTY_COUNTRY.value.toString()
            )

            if (!signUpData.isMarketPlaceTermAndCondition) throw SignUpValidationException(
                    SignUpFieldsValidation.EMPTY_TERMS_CONDITIONS.value.toString()
            )
        }

        return true
    }


}