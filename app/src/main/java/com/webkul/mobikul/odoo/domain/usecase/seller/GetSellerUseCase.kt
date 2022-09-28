package com.webkul.mobikul.odoo.domain.usecase.seller

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity
import com.webkul.mobikul.odoo.domain.repository.SellerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSellerUseCase @Inject constructor(
		private val sellerRepository : SellerRepository
) {
	
	operator fun invoke(sellerId : Int?) : Flow<Resource<SellerDetailsEntity>> = flow {
		emit(Resource.Loading)
		
		if (sellerId != null) {
			val result = sellerRepository.get(sellerId)
			emit(result)
		} else emit(Resource.Failure(FailureStatus.API_FAIL))
		
	}.flowOn(Dispatchers.IO)
}