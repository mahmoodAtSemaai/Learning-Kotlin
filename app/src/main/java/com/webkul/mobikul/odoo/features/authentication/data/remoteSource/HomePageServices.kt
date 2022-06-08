package com.webkul.mobikul.odoo.features.authentication.data.remoteSource

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.features.authentication.data.models.BaseOtpLoginResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import retrofit2.http.GET

interface HomePageServices {

    @GET(ApiInterface.MOBIKUL_CATALOG_HOME_PAGE_DATA)
    suspend fun getHomePageData(): HomePageResponse

}