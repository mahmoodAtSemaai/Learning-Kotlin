package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.domain.repository.TermsConditionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ViewTnCUseCase @Inject constructor(private val termsConditionRepository: TermsConditionRepository) {

    operator fun invoke(): Flow<Resource<TermsAndConditionsEntity>> = flow {

        emit(Resource.Loading)
        val result = termsConditionRepository.getTermAndCondition()
        emit(result)

    }.flowOn(Dispatchers.IO)

}