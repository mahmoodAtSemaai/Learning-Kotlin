package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ShippingMethodListEntity
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest

interface ShippingMethodRepository : Repository<Any,Any,Any> {

    suspend fun updateShippingMethodForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) : Resource<Any>

    suspend fun getActiveShippingMethods(partnerId: String) : Resource<ShippingMethodListEntity>
}