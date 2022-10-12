package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EditAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    operator fun invoke(url: String,
                        addressRequestBody: AddressRequestBody): Flow<Resource<Any>> = flow {

        emit(Resource.Loading)
        val result = addressRepository.editAddress( url, addressRequestBody)
        emit(result)
    }.flowOn(Dispatchers.IO)

}
