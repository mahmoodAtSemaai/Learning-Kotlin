package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.data.response.UserDetailResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import retrofit2.http.GET
import retrofit2.http.Path

interface UserDetailServices {
    companion object {
        const val USER_ID = "user_id"
        const val USER_DETAILS = "/v1/users/{$USER_ID}"
    }

    @GET(USER_DETAILS)
    suspend fun getUserDetails(
            @Path(USER_ID) userId: String
    ) : BaseResponseNew<UserDetailResponse>
}