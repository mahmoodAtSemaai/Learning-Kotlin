package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.entity.UserAddressEntity
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAddressServices {
    @PUT(ApiInterface.USER_ADDRESS)
    suspend fun setUserAddress(
        @Path(ApiInterface.PARTNER_ID) partnerId: String,
        @Body userAddressRequest: String
    ): BaseUserOnboardingResponse<UserAddressEntity>
}