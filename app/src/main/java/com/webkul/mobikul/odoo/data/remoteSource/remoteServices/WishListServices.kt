package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface WishListServices {

    @HTTP(
        method = ApiInterface.DELETE,
        path = ApiInterface.REMOVE_FROM_WISHLIST_API,
        hasBody = true
    )
    suspend fun deleteProductFromWishlist(
        @Body wishlistRequestBody: String?
    ): WishListUpdatedResponse


    @POST(ApiInterface.ADD_TO_WISHLIST_API)
    suspend fun addProductToWishlist(
        @Body addToWishlistStrReq: String?
    ): WishListUpdatedResponse
}