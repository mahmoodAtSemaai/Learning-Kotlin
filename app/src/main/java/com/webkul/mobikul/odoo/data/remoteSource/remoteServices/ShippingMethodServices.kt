package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.connection.ApiInterface.PARTNER_ID
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.data.response.models.CheckoutBaseResponse
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse
import retrofit2.http.*
import java.util.ArrayList

interface ShippingMethodServices {

    @GET(ApiInterface.MOBIKUL_CHECKOUT_SHIPPING_METHODS_V1)
    suspend fun getActiveShippingMethods(@Query(PARTNER_ID) partnerId: String) : CheckoutBaseResponse<ArrayList<ActiveShippingMethod>>

    @PUT(ApiInterface.MOBIKUL_UPDATE_ORDER_DATA)
    suspend fun updateShippingMethodForOrder(
        @Path(ApiInterface.MOBIKUL_ORDER_ID) orderId: Int,
        @Body updateAddressRequest: String) : BaseResponse
}