package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.entity.UserDetailsEntity
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserDetailsServices {

    @PUT(ApiInterface.USER_DETAILS)
    suspend fun setUserDetails(
        @Path(ApiInterface.USER_ID) userId: String,
        @Path(ApiInterface.CUSTOMER_ID) customerId: String,
        @Body userDetailsRequest: String
    ): BaseUserOnboardingResponse<UserDetailsEntity>

}