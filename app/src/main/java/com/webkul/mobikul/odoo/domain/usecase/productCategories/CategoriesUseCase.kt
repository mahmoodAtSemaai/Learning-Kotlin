package com.webkul.mobikul.odoo.domain.usecase.productCategories

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductCategoriesEntity
import com.webkul.mobikul.odoo.domain.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
        private val categoriesRepository: CategoriesRepository
) {

    operator fun invoke(): Flow<Resource<ProductCategoriesEntity>> = flow {
        emit(Resource.Loading)
        val result = categoriesRepository.getCategories()
        emit(result)
    }.flowOn(Dispatchers.IO)
}