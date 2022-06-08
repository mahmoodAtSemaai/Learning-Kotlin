package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface SplashServices {

    @POST(ApiInterface.MOBIKUL_EXTRAS_SPLASH_PAGE_DATA)
    suspend fun getSplashPageData(): SplashScreenResponse

}