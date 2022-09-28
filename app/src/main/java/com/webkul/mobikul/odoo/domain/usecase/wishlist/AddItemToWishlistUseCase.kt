package com.webkul.mobikul.odoo.domain.usecase.wishlist

import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.core.utils.ResourcesProvider
import com.webkul.mobikul.odoo.data.request.WishListRequest
import com.webkul.mobikul.odoo.domain.repository.WishListRepository
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddItemToWishlistUseCase @Inject constructor(
    private val wishListRepository: WishListRepository,
    private val firebaseAnalyticsImpl: FirebaseAnalyticsImpl,
    private val resourcesProvider: ResourcesProvider
) : UseCase {

    suspend operator fun invoke(productId: String?, productName: String?): Flow<Resource<String?>> =
        flow {

            emit(Resource.Loading)
            if (productId.isNullOrBlank().not()) {
                when (val result =
                    wishListRepository.update(WishListRequest(productId!!.toInt()))) {

                    is Resource.Success -> {
                        firebaseAnalyticsImpl.logAddToWishlistEvent(productId, productName)
                        emit(Resource.Success(result.value.message))
                    }

                    is Resource.Failure -> emit(result)
                }

            } else emit(
                Resource.Failure(
                    FailureStatus.API_FAIL, null, resourcesProvider.getString(R.string.error_could_not_add_to_wishlist_pls_try_again)
                )
            )
        }
}