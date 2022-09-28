package com.webkul.mobikul.odoo.domain.usecase.wishlist

import android.os.SystemClock
import com.webkul.mobikul.odoo.core.domain.UseCase
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductWishlistedEntity
import javax.inject.Inject

class ProductWishlistedUseCase @Inject constructor() : UseCase {
	
	suspend operator fun invoke(
		lastClickTime : Long, data : ProductEntity
	) : Resource<ProductWishlistedEntity> {
		
		return if (SystemClock.elapsedRealtime() - lastClickTime > 1000) {
			val productWishlistedEntity =
				ProductWishlistedEntity(SystemClock.elapsedRealtime(), data.addedToWishlist)
			(Resource.Success(productWishlistedEntity))
		} else {
			(Resource.Default)
		}
		
	}
}