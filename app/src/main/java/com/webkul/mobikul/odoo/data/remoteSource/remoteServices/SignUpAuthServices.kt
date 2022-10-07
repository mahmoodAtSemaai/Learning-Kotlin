package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.response.SignUpV1Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpAuthServices {

    @POST(ApiInterface.SIGN_UP_WITH_OTP)
    suspend fun signUpWithOTP(@Body signUpOtpAuthRequest: String?): BaseOtpSignUpResponse<SignUpV1Response>

}