package com.webkul.mobikul.odoo.domain.usecase.product

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.domain.repository.ProductSellersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetProductSellersUseCase @Inject constructor(
        private val productSellersRepository: ProductSellersRepository
) {

    operator fun invoke(globalProductID: Int?, fromProductTemplate: Int?): Flow<Resource<ProductListEntity>> = flow {
        emit(Resource.Loading)

        if (globalProductID != null && fromProductTemplate != null) {
            val result = productSellersRepository.get(globalProductID, fromProductTemplate)
            emit(result)
        } else emit(Resource.Failure(FailureStatus.API_FAIL))
    }.flowOn(Dispatchers.IO)
}