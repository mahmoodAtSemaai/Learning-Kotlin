package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMTokenServices {

    @POST(ApiInterface.MOBIKUL_EXTRAS_REGISTER_FCM_TOKEN)
    suspend fun registerDeviceToken(
            @Body registerDeviceTokenRequestStr: String?
    ): BaseResponse

}