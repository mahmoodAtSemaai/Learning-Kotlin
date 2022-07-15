package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.CountryStateServices
import javax.inject.Inject

class CountryStateRemoteDataSource @Inject constructor(
        private val apiService: CountryStateServices,
        gson: Gson,
        appPreferences: AppPreferences
) : BaseRemoteDataSource(gson, appPreferences) {

    suspend fun getCountryState() = safeApiCall(CountryEntity::class.java) {
        apiService.getCountryState()
    }
}