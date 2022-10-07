package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.data.response.BaseResponse
import com.webkul.mobikul.odoo.data.response.OtpAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthenticationServices {

    @GET(ApiInterface.VALIDATE_PHONE_NUMBER)
    suspend fun validatePhoneNumber(@Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?): BaseResponse<UserCreateDateEntity>

    @POST(ApiInterface.LOGIN_WITH_OTP_JWT_TOKEN)
    suspend fun loginViaOtpJWTToken(
        @Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?,
        @Body otpAuthenticationRequest: String?
    ): BaseResponse<OtpAuthenticationResponse>

    @POST(ApiInterface.LOGIN_WITH_JWT_TOKEN)
    suspend fun loginViaJWTToken(
        @Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?,
        @Body otpLoginAuthenticationRequest: String?
    ): BaseResponse<OtpAuthenticationResponse>

}