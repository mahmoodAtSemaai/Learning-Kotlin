package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.data.response.SellerResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import retrofit2.http.GET
import retrofit2.http.Path

interface SellerServices {

    companion object {
        const val SELLER_ID = "seller_id"
        const val GET_SELLER = "v1/sellers/{$SELLER_ID}"
    }

    @GET(GET_SELLER)
    suspend fun get(@Path(SELLER_ID) sellerId: Int) : BaseResponseNew<SellerResponse>
}