package com.webkul.mobikul.odoo.domain.usecase.productCategories

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.domain.repository.CategoryProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryProductsUseCase @Inject constructor(
		private val categoryProductRepository : CategoryProductRepository
) {
	operator fun invoke(categoryId : String, offset : Int, limit : Int) : Flow<Resource<ProductListEntity>> = flow {
		emit(Resource.Loading)
		val result = categoryProductRepository.get(categoryId.toInt(),true, offset, limit)
		emit(result)
	}.flowOn(Dispatchers.IO)
}