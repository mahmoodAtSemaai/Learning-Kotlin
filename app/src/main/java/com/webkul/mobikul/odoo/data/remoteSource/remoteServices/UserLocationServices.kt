package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.entity.UserLocationEntity
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserLocationServices {
    @PUT(ApiInterface.USER_ADDRESS)
    suspend fun setUserLocation(
        @Path(ApiInterface.PARTNER_ID) partnerId: String,
        @Body userLocationRequest: String
    ): BaseUserOnboardingResponse<UserLocationEntity>
}