package com.webkul.mobikul.odoo.features.auth.domain.usecase

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.features.auth.domain.repo.SignUpRepository
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ViewMarketPlaceTnCUseCase @Inject constructor(private val signUpRepository: SignUpRepository) {

    operator fun invoke(): Flow<Resource<TermAndConditionResponse>> = flow {

        emit(Resource.Loading)
        val result = signUpRepository.getSellerTerms()

        when (result) {
            is Resource.Success -> {
                if (result.value.isSuccess) emit(result)
                else emit(Resource.Failure( failureStatus = FailureStatus.API_FAIL , message = result.value.message))
            }
            else -> emit(result)
        }

    }.flowOn(Dispatchers.IO)

}