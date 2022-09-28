package com.webkul.mobikul.odoo.domain.usecase.search

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSearchUseCase @Inject constructor(
        private val searchRepository: SearchRepository
) {

    operator fun invoke(searchQuery: String): Flow<Resource<ProductListEntity>> = flow {
        emit(Resource.Loading)
        val result = searchRepository.get(searchQuery)
        emit(result)
    }.flowOn(Dispatchers.IO)
}