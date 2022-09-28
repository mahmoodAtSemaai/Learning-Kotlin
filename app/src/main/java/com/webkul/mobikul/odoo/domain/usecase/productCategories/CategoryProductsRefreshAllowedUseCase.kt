package com.webkul.mobikul.odoo.domain.usecase.productCategories

import com.webkul.mobikul.odoo.core.utils.Resource
import javax.inject.Inject

class CategoryProductsRefreshAllowedUseCase @Inject constructor() {
	operator fun invoke(
		lastCompletelyVisibleItemPosition : Int,
		rvItemCount : Int,
		tCount : Int,
		dataRequested : Boolean
	) : Resource<Boolean> {
		
		return Resource.Success(
			lastCompletelyVisibleItemPosition == rvItemCount - 1 &&
					rvItemCount < tCount && !dataRequested
		)
		
	}
}