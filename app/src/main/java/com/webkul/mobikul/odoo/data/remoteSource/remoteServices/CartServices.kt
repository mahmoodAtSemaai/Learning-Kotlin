package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.connection.ApiInterface.CART_ID
import com.webkul.mobikul.odoo.connection.ApiInterface.PARTNER_ID
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.data.response.models.CartProductsResponse
import com.webkul.mobikul.odoo.data.response.models.GetCartId
import com.webkul.mobikul.odoo.model.product.AddToCartResponse
import retrofit2.http.*

interface CartServices {

    @GET(ApiInterface.CHECK_IF_CART_EXISTS)
    suspend fun getCart(@Query(PARTNER_ID) partnerId: Int): CartBaseResponse<GetCartId>

    @POST(ApiInterface.CREATE_CART)
    suspend fun createCart(@Query(PARTNER_ID) partnerId: Int): CartBaseResponse<GetCartId>

    @PUT(ApiInterface.ADD_TO_CART_API)
    suspend fun addToCart(@Path(CART_ID) cartId: Int, @Body products: String): CartBaseResponse<CartProductsResponse>
}