package com.webkul.mobikul.odoo.data.remoteSource.remoteServices

import com.webkul.mobikul.odoo.connection.ApiInterface
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import retrofit2.http.POST

interface CountryStateServices {

    @POST(ApiInterface.MOBIKUL_EXTRAS_COUNTRY_STATE_DATA)
    suspend fun getCountryState(): CountryStateData

}