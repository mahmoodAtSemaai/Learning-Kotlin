package com.webkul.mobikul.odoo.data.remoteSource.remoteServices


import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SplashServices {

    @POST(ApiInterface.MOBIKUL_ANALYTICS)
    suspend fun getUserAnalyticsDetails(@Body registerDeviceTokenRequestStr: String?): UserAnalyticsResponse

    @POST(ApiInterface.MOBIKUL_EXTRAS_SPLASH_PAGE_DATA)
    suspend fun getSplashPageData(): SplashScreenResponse
}