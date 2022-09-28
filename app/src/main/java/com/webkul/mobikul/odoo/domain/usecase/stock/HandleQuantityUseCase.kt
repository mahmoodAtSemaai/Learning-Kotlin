package com.webkul.mobikul.odoo.domain.usecase.stock

import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.QuantityEntity
import com.webkul.mobikul.odoo.domain.enums.QuantityWarningType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HandleQuantityUseCase @Inject constructor() {
	
	suspend operator fun invoke(currentQty : Int?, data : ProductEntity?) : Flow<Resource<QuantityEntity>> = flow {
		if (data != null && currentQty != null) {
			when {
				(data.isOutOfStock() && !data.isService()) -> {
					data.setCurrentQuantity(data.availableQuantity)
					emit(Resource.Success(QuantityEntity(data, QuantityWarningType.OUT_OF_STOCK.value)))
				}
				!data.isNever() && !data.isPreOrder() && !data.isCustom() && !data.isService() && currentQty > data.availableQuantity -> {
					data.setCurrentQuantity(data.availableQuantity)
					emit(Resource.Success(QuantityEntity(data, QuantityWarningType.LIMITED_STOCK.value)))
				}
				else -> {
					data.setCurrentQuantity(currentQty)
					emit(Resource.Success(QuantityEntity(data, QuantityWarningType.NONE.value)))
				}
			}
		} else emit(Resource.Failure(FailureStatus.API_FAIL))
	}
}