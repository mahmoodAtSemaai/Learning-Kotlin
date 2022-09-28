package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddToWishlistEntity
import com.webkul.mobikul.odoo.data.entity.RemoveFromWishlistEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.WishListRemoteDataSource
import com.webkul.mobikul.odoo.data.request.WishListRequest
import com.webkul.mobikul.odoo.domain.repository.WishListRepository
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest
import javax.inject.Inject

class WishListRepositoryImpl @Inject constructor(
    private val remoteDataSource: WishListRemoteDataSource,
    private val appPreferences : AppPreferences,
    ) : WishListRepository {

    override suspend fun update(wishListRequest: WishListRequest): Resource<AddToWishlistEntity> {
        return remoteDataSource.update(wishListRequest)
    }

    override suspend fun delete(wishListRequest: WishListRequest): Resource<RemoveFromWishlistEntity> {
        return remoteDataSource.delete(wishListRequest)
    }
    
    override suspend fun isWishlistAllowed() : Resource<Boolean> {
        return try {
            val result = appPreferences.isAllowedWishlist && appPreferences.isLoggedIn
            Resource.Success(result)
        } catch (e : Exception) {
            Resource.Failure(FailureStatus.OTHER, null, e.localizedMessage)
        }
    }
}