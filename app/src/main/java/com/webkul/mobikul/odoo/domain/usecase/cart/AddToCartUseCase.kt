package com.webkul.mobikul.odoo.domain.usecase.cart

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.request.CartProductItemRequest
import com.webkul.mobikul.odoo.data.request.CartProductsRequest
import com.webkul.mobikul.odoo.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val firebaseAnalyticsImpl: FirebaseAnalyticsImpl,
    private val resourcesProvider: ResourcesProvider
) : UseCase {

    operator fun invoke(productId: String?, quantity: Int?, cartId: Int): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading)

            if (productId.isNullOrBlank().not() && quantity != null) {

                when (val result = cartRepository.update(
                    CartProductsRequest(
                        arrayListOf(
                            CartProductItemRequest(
                                productId?.toInt() ?: -1, quantity, 0
                            )
                        ), cartId
                    )
                )) {
                    is Resource.Success -> {
                        firebaseAnalyticsImpl.logAddToCartEvent(
                            productId!!,
                            result.value.productName
                        )
                        emit(Resource.Success(true))
                    }
                    is Resource.Failure -> emit(result)
                }
            } else emit(
                Resource.Failure(
                    FailureStatus.API_FAIL,
                    null,
                    resourcesProvider.getString(R.string.error_could_not_add_to_bag_pls_try_again)
                )
            )

        }.flowOn(Dispatchers.IO)

}
