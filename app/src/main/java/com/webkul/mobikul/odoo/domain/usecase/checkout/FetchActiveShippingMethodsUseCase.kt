package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity
import com.webkul.mobikul.odoo.domain.repository.ShippingMethodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchActiveShippingMethodsUseCase @Inject constructor(
    private val shippingMethodRepository: ShippingMethodRepository
) {

    operator fun invoke(partnerId: String): Flow<Resource<ShippingMethodListEntity>> = flow {

        emit(Resource.Loading)
        val result = shippingMethodRepository.getActiveShippingMethods(partnerId)
        emit(result)

    }.flowOn(Dispatchers.IO)

}