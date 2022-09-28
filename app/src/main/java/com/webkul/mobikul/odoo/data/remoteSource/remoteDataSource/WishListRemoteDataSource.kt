package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.AddToWishlistEntity
import com.webkul.mobikul.odoo.data.entity.RemoveFromWishlistEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.WishListServices
import com.webkul.mobikul.odoo.data.request.WishListRequest
import javax.inject.Inject

class WishListRemoteDataSource @Inject constructor(
    private val wishListServices: WishListServices,
    gson: Gson,
    appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun update(wishListRequest: WishListRequest) =
        safeApiCall(AddToWishlistEntity::class.java) {
            wishListServices.addProductToWishlist(wishListRequest.toString())
        }

    suspend fun delete(wishListRequest: WishListRequest) =
        safeApiCall(RemoveFromWishlistEntity::class.java) {
            wishListServices.deleteProductFromWishlist(wishListRequest.toString())
        }

}