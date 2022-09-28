package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CartEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.CartRemoteDataSource
import com.webkul.mobikul.odoo.data.request.CartProductsRequest
import com.webkul.mobikul.odoo.domain.repository.CartRepository
import com.webkul.mobikul.odoo.model.request.AddToBagRequest
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val updateCartRemoteDataSource: CartRemoteDataSource,
    private val appPreferences: AppPreferences
) : CartRepository {

    override suspend fun get(partnerId: Int): Resource<CartEntity> {
        val result = updateCartRemoteDataSource.get(partnerId)
        when (result) {
            is Resource.Success -> {
                appPreferences.cartId = result.value.cartId
            }
        }
        return result
    }

    override suspend fun create(partnerId: Int): Resource<CartEntity> {
        val result = updateCartRemoteDataSource.create(partnerId)
        when (result) {
            is Resource.Success -> {
                appPreferences.cartId = result.value.cartId
            }
        }
        return result
    }

    override suspend fun update(cartProductsRequest: CartProductsRequest): Resource<CartEntity> {
        val result = updateCartRemoteDataSource.update(cartProductsRequest)

        when (result) {
            is Resource.Success -> appPreferences.newCartCount = result.value.cartCount
        }

        return result
    }

}