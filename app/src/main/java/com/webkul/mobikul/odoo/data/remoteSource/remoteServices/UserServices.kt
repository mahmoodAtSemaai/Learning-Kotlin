package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.entity.UserOnboardingStageEntity
import com.webkul.mobikul.odoo.data.response.BaseUserOnboardingResponse
import com.webkul.mobikul.odoo.data.response.UserOnboardingStageResponse
import com.webkul.mobikul.odoo.data.response.UserResponse
import retrofit2.http.*

interface UserServices {

    @PUT(ApiInterface.USER_DETAILS)
    suspend fun updateUserDetails(
        @Path(ApiInterface.USER_ID) userId: String,
        @Path(ApiInterface.CUSTOMER_ID) customerId: String,
        @Body userDetailsRequest: String
    ): BaseUserOnboardingResponse<UserResponse>

    @GET(ApiInterface.USER_ONBOARDING_STAGE)
    suspend fun getUserOnboardingStage(@Query(ApiInterface.USER_ID) userId: String): BaseUserOnboardingResponse<UserOnboardingStageResponse>

}