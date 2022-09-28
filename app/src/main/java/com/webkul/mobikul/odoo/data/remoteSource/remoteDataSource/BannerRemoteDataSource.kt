package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.BannerListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.BannerServices
import javax.inject.Inject

class BannerRemoteDataSource @Inject constructor(
        private val bannerServices: BannerServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get() = safeApiCall(BannerListEntity::class.java) {
        bannerServices.getBanners()
    }

}