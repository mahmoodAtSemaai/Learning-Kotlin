package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.data.response.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.data.response.BaseResponse
import com.webkul.mobikul.odoo.data.response.GenerateOTPResponse
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import retrofit2.http.*

interface OTPServices {

    @GET(ApiInterface.GENERATE_OTP)
    suspend fun generateOTP(@Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?): GenerateOTPResponse

    @POST(ApiInterface.GENERATE_SIGN_UP_OTP)
    suspend fun generateOTPSignUp(@Query(ApiInterface.PHONE_NUMBER) phoneNumber: String?): BaseOtpLoginResponse<Any>

}