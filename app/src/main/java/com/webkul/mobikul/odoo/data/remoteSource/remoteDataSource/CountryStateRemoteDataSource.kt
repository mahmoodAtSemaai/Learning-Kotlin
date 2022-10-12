package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.CountryStateDataStore
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.CountryStateServices
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import javax.inject.Inject

class CountryStateRemoteDataSource @Inject constructor(
        private val apiService: CountryStateServices,
        gson: Gson,
        appPreferences: AppPreferences
) : CountryStateDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get() = safeApiCall {
        apiService.getCountryState()
    }
}