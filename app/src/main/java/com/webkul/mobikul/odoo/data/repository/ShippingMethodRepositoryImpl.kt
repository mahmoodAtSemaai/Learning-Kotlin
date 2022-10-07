package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.ShippingMethodRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.ShippingMethodRepository
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import javax.inject.Inject

class ShippingMethodRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShippingMethodRemoteDataSource
) : ShippingMethodRepository {

     override suspend fun updateShippingMethodForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) : Resource<Any> {
        return remoteDataSource.updateShippingMethodForOrder(orderId, updateOrderRequest)
    }

    override suspend fun getActiveShippingMethods(partnerId: String) : Resource<ShippingMethodListEntity> {
        return remoteDataSource.getActiveShippingMethods(partnerId)
    }
}