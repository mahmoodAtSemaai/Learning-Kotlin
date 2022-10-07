package com.webkul.mobikul.odoo.domain.usecase.auth

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.TermsAndConditionsEntity
import com.webkul.mobikul.odoo.domain.repository.SellerTermsConditionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ViewMarketPlaceTnCUseCase @Inject constructor(private val sellerTermsConditionRepository: SellerTermsConditionRepository) {

    operator fun invoke(): Flow<Resource<TermsAndConditionsEntity>> = flow {
        emit(Resource.Loading)
        val result = sellerTermsConditionRepository.get()
        emit(result)
    }.flowOn(Dispatchers.IO)

}