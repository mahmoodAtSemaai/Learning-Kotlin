package com.webkul.mobikul.odoo.domain.usecase.wishlist

import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.repository.WishListRepository
import javax.inject.Inject

class IsWishListAllowedUseCase @Inject constructor(
	private val repository : WishListRepository
) : UseCase {
    
    suspend operator fun invoke() : Resource<Boolean> {
        return repository.isWishlistAllowed()
    }
}