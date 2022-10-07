package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.DeleteAddressEntity
import com.webkul.mobikul.odoo.domain.enums.UpdateAddressException
import com.webkul.mobikul.odoo.domain.enums.UpdateAddressValidation
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    operator fun invoke(address: AddressData): Flow<Resource<DeleteAddressEntity>> = flow {

        emit(Resource.Loading)
        val result = addressRepository.deleteAddress(address.url)
        emit(result)


    }.flowOn(Dispatchers.IO)

}
