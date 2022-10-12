package com.webkul.mobikul.odoo.domain.usecase.checkout

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ShippingMethodEntity
import com.webkul.mobikul.odoo.domain.enums.*
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.domain.repository.ShippingMethodRepository
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequestModel
import com.webkul.mobikul.odoo.model.customer.address.AddressData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateShippingMethodForOrderUseCase @Inject constructor(
    private val shippingMethodRepository: ShippingMethodRepository
) {

    @Throws(UpdateShippingMethodException::class)
    operator fun invoke(orderId: Int, shippingMethodEntity: ShippingMethodEntity?): Flow<Resource<Any>> = flow {

        emit(Resource.Loading)
        if (shippingMethodEntity == null) throw UpdateShippingMethodException(UpdateShippingMethodValidation.NULL_SHIPPING_METHOD.value.toString())
        else {
            val result = shippingMethodRepository.updateShippingMethodForOrder(
                orderId,
                UpdateOrderRequest(UpdateOrderRequestModel(shippingMethodEntity.id, null))
            )
            emit(result)
        }

    }.flowOn(Dispatchers.IO)

}
