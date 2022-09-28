package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.SellerServices
import javax.inject.Inject

class SellerRemoteDataSource @Inject constructor(
        private val sellerServices: SellerServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun get(sellerId: Int) = safeApiCall(SellerDetailsEntity::class.java) {
        sellerServices.get(sellerId)
    }

}