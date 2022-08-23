package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.response.BaseOtpSignUpResponse
import com.webkul.mobikul.odoo.data.entity.SignUpV1Entity
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpAuthServices {

    @POST(ApiInterface.GENERATE_SIGN_UP_OTP)
    suspend fun generateOTP(@Query(ApiInterface.PHONE_NUMBER) phoneNumber: String?): BaseOtpLoginResponse<Any>

    @POST(ApiInterface.SIGN_UP_WITH_OTP)
    suspend fun signUpWithOTP(@Body signUpOtpAuthRequest: String?): BaseOtpSignUpResponse<SignUpV1Entity>

}