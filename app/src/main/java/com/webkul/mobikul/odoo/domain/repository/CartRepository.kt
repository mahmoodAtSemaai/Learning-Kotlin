package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CartEntity
import com.webkul.mobikul.odoo.data.request.CartProductsRequest

interface CartRepository : Repository {
    suspend fun get(partnerId: Int): Resource<CartEntity>
    suspend fun create(partnerId: Int): Resource<CartEntity>
    suspend fun update(cartProductsRequest: CartProductsRequest): Resource<CartEntity>

}