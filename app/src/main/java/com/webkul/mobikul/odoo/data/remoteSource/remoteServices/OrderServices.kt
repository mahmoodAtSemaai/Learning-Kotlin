package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface.*
import com.webkul.mobikul.odoo.data.response.models.CheckoutBaseResponse
import com.webkul.mobikul.odoo.data.response.models.GetDiscountPriceResponse
import com.webkul.mobikul.odoo.model.checkout.OrderDataResponse
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse
import retrofit2.http.*

interface OrderServices {

    @GET(MOBIKUL_GET_ORDER_DATA_V2)
    suspend fun getOrderData(@Path(MOBIKUL_ORDER_ID) cartId: Int,
                             @Query(value = LINEIDS, encoded = true) lineIds: String
    ): CheckoutBaseResponse<OrderDataResponse>

    @GET(REDEEM_SEMAAI_POINTS)
    suspend fun getDiscountPrice(@Query(ORDER_ID) cartId: Int,
                                 @Query(value = LINEIDS, encoded = true) lineIds: String
    ): CheckoutBaseResponse<GetDiscountPriceResponse>

    @GET(MOBIKUL_GET_ORDER_DATA)
    suspend fun getOrderDataAfterCheckout(
        @Path(MOBIKUL_ORDER_ID)  orderID: Int,
        @Query(POINTS_REDEEM)  pointsRedeem: Boolean
    ): OrderDataResponse

    @POST(LOYALTY_CHECKOUT_ORDER_REVIEW_V3)
    suspend fun getOrderReviewData(@Body orderReviewString : String
    ): CheckoutBaseResponse<OrderReviewResponse>

    @POST(LOYALTY_CHECKOUT_PLACE_ORDER)
    suspend fun placeOrder(@Body placeOrderRequest : String
    ) : OrderPlaceResponse

}