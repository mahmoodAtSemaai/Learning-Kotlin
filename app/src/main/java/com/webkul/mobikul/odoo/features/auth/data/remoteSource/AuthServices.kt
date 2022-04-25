package com.webkul.mobikul.odoo.features.auth.data.remoteSource


import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {

    @POST(ApiInterface.MOBIKUL_CUSTOMER_SIGN_IN)
    suspend fun signIn(@Body registerDeviceTokenRequestStr: String?): LoginResponse


    @POST(ApiInterface.MOBIKUL_CUSTOMER_SIGN_UP)
    suspend fun signUp(@Body signUpReqeustJsonStr: String?): SignUpResponse
}