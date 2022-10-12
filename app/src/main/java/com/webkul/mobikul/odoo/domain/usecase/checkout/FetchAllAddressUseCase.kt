package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchAllAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    operator fun invoke(): Flow<Resource<AddressEntity>> = flow {

        emit(Resource.Loading)
        val result = addressRepository.get(BaseLazyRequest(0, 0))
        emit(result)

    }.flowOn(Dispatchers.IO)

}