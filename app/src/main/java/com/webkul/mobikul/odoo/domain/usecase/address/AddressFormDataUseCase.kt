package com.webkul.mobikul.odoo.domain.usecase.address

import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddressFormDataUseCase @Inject constructor(
    private val repository: AddressRepository) : UseCase {

    operator fun invoke(addressData: AddressData): Flow<Resource<AddressFormEntity>> = flow {

        emit(Resource.Loading)
        val result = repository.getAddressFormData(addressData.url)
        emit(result)
    }
}