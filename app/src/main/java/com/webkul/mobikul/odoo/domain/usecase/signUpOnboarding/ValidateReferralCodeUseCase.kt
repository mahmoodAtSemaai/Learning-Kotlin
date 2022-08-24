package com.webkul.mobikul.odoo.domain.usecase.signUpOnboarding

import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.enums.ReferralCodeValidation
import com.webkul.mobikul.odoo.domain.enums.ReferralCodeValidationException
import com.webkul.mobikul.odoo.domain.repository.ReferralCodeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ValidateReferralCodeUseCase@Inject constructor(
    private val referralCodeRepository: ReferralCodeRepository
)  {
    @Throws(ReferralCodeValidationException::class)
    operator fun invoke(
        referralCode:String
    ): Flow<Resource<Any>> = flow {

        emit(Resource.Default)
        if(referralCode.length == ApplicationConstant.REFERRAL_CODE_LENGTH){
            emit(Resource.Loading)
            val result = referralCodeRepository.validateReferralCode(referralCode)
            when(result) {
                is Resource.Failure -> {
                    throw ReferralCodeValidationException(ReferralCodeValidation.INVALID_REFERRAL_CODE.value.toString())
                }
            }
            emit(result)
        }
    }.flowOn(Dispatchers.IO)
}