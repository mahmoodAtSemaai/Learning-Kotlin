package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.data.response.BannerListResponse
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew
import retrofit2.http.GET

interface BannerServices {

    companion object {
        const val BANNERS = "v1/banners?source=app"
    }

    @GET(BANNERS)
    suspend fun getBanners(): BaseResponseNew<BannerListResponse>
}