package com.webkul.mobikul.odoo.domain.usecase.product

import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.domain.repository.ProductDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
	private val productDetailsRepository : ProductDetailsRepository
) : UseCase {
	
	operator fun invoke(templateId : Int?) : Flow<Resource<ProductEntity>> = flow {
		emit(Resource.Loading)
		if (templateId != null) {
			val result = productDetailsRepository.get(templateId)
			emit(result)
		} else emit(
			Resource.Failure(
				FailureStatus.API_FAIL,
			)
		)
		
	}.flowOn(Dispatchers.IO)
	
}