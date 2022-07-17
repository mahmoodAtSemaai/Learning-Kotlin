package com.webkul.mobikul.odoo.data.remoteSource.remoteServices


import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import retrofit2.http.GET

interface HomeServicesV1 {

    @GET(ApiInterface.MOBIKUL_CATALOG_HOME_PAGE_DATA)
    suspend fun getHomePageData(): HomePageResponse
}