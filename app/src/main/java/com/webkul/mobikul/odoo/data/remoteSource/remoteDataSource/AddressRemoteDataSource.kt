package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.NetworkModelParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.CountryEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import javax.inject.Inject

class AddressRemoteDataSource @Inject constructor(
        private val apiService: AddressServices
) : BaseRemoteDataSource() {

    suspend fun getAddressBookData(baseLazyRequest: BaseLazyRequest) = safeApiCall {
        val response = apiService.getAddressBookData(baseLazyRequest.toString())
        NetworkModelParser(
                AddressEntity::class.java,
                MyAddressesResponse::class.java
        ).toObject(response)
    }

    suspend fun getCountryStateData() = safeApiCall {
        val response = apiService.getCountryStateData()
        NetworkModelParser(
                CountryEntity::class.java,
                CountryStateData::class.java
        ).toObject(response)
    }
}