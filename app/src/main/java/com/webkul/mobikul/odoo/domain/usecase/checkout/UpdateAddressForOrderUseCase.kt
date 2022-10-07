package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.enums.*
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequestModel
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateAddressForOrderUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    @Throws(UpdateAddressException::class)
    operator fun invoke(orderId: Int, address: AddressData?): Flow<Resource<Any>> = flow {

        emit(Resource.Loading)
        if (address == null) throw UpdateAddressException(UpdateAddressValidation.NULL_ADDRESS.value.toString())
        else if (address.displayName.isEmpty()) throw UpdateAddressException(UpdateAddressValidation.EMPTY_DISPLAY_NAME.value.toString())
        else {
            val result = addressRepository.updateAddressForOrder(
                orderId,
                UpdateOrderRequest(UpdateOrderRequestModel(null, address.addressId.toInt()))
            )
            emit(result)
        }

    }.flowOn(Dispatchers.IO)

}
