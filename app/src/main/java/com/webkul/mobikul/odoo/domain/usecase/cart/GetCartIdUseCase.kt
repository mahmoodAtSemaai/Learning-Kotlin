package com.webkul.mobikul.odoo.domain.usecase.cart

import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CartEntity
import com.webkul.mobikul.odoo.domain.repository.CartRepository
import com.webkul.mobikul.odoo.domain.repository.UserDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCartIdUseCase @Inject constructor(
    private val userDetailRepository: UserDetailRepository,
    private val cartRepository: CartRepository
) {

    operator fun invoke(): Flow<Resource<CartEntity>> = flow {
        emit(Resource.Loading)
        when (val result = userDetailRepository.getUserDetails()) {
            is Resource.Success -> {
                if(result.value.cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE ){
                val cart = cartRepository.get(result.value.customerId)
                emit(cart)
                }else{
                    emit(Resource.Success(CartEntity("",0, emptyList(),result.value.cartId)))
                }
            }

            else -> {
                emit(Resource.Failure(FailureStatus.OTHER))
            }
        }

    }.flowOn(Dispatchers.IO)

}