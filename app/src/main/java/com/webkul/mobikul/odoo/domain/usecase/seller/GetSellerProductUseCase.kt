package com.webkul.mobikul.odoo.domain.usecase.seller

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.domain.repository.SellerProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSellerProductUseCase @Inject constructor(
        private val sellerProductRepository: SellerProductRepository
) {

    operator fun invoke(sellerId: Int?): Flow<Resource<ProductListEntity>> = flow {
        emit(Resource.Loading)
        
        if (sellerId !=null ) {
            val result = sellerProductRepository.get(sellerId)
            emit(result)
        }else emit(Resource.Failure(FailureStatus.API_FAIL))
        
    }.flowOn(Dispatchers.IO)
}