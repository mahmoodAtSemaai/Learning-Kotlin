package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.data.entity.UserCreateDateEntity
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.features.authentication.data.models.OtpAuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthenticationServices {

    @GET(ApiInterface.VALIDATE_PHONE_NUMBER)
    suspend fun validatePhoneNumber(@Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?): BaseOtpLoginResponse<UserCreateDateEntity>

    @GET(ApiInterface.GENERATE_OTP)
    suspend fun generateOTP(@Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?): BaseOtpLoginResponse<Any>

    @POST(ApiInterface.LOGIN_WITH_OTP_JWT_TOKEN)
    suspend fun loginViaOtpJWTToken(
        @Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?,
        @Body otpAuthenticationRequest: String?
    ): BaseOtpLoginResponse<OtpAuthenticationResponse>

    @POST(ApiInterface.LOGIN_WITH_JWT_TOKEN)
    suspend fun loginViaJWTToken(
        @Path(ApiInterface.PHONE_NUMBER) phoneNumber: String?,
        @Body otpLoginAuthenticationRequest: String?
    ): BaseOtpLoginResponse<OtpAuthenticationResponse>

}