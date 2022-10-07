package com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource

import com.google.gson.Gson
import com.webkul.mobikul.odoo.core.data.local.AppPreferences
import com.webkul.mobikul.odoo.core.data.remote.BaseRemoteDataSource
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSourceInterface.AddressDataStore
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteServices.AddressServices
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.generic.CountryStateData
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import retrofit2.http.Body
import javax.inject.Inject

class AddressRemoteDataSource @Inject constructor(
        private val apiService: AddressServices,
        gson: Gson,
        appPreferences: AppPreferences
) : AddressDataStore, BaseRemoteDataSource(gson, appPreferences) {

    override suspend fun get(request: BaseLazyRequest) = safeApiCall {
        apiService.getAddress(request.toString())
    }

    suspend fun updateAddressForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) = safeApiCall(Any::class.java) {
        apiService.updateAddressForOrder(orderId, updateOrderRequest.toString())
    }


    suspend fun deleteAddress(url: String) = safeApiCall(DeleteAddressEntity::class.java) {
        apiService.deleteAddress(url)
    }

    suspend fun getAddressFormData(url: String) = safeApiCall(AddressFormEntity::class.java) {
        apiService.getAddressFormData(url)
    }

    suspend fun getStates(companyId: Int) = safeApiCall(StateListEntity::class.java) {
        apiService.getStates(companyId)
    }

    suspend fun getDistricts(stateId: Int) = safeApiCall(DistrictListEntity::class.java) {
        apiService.getDistricts(stateId)
    }

    suspend fun getSubDistricts(districtId: Int) = safeApiCall(SubDistrictListEntity::class.java) {
        apiService.getSubDistricts(districtId)
    }

    suspend fun getVillages(subDistrictId: Int) = safeApiCall(VillageListEntity::class.java) {
        apiService.getVillages(subDistrictId)
    }

    suspend fun editAddress( url: String, addressRequestBody: AddressRequestBody) = safeApiCall(Any::class.java) {
        apiService.editAddress(url, addressRequestBody.newAddressData)
    }

    suspend fun addAddress(addressRequestBody: AddressRequestBody) = safeApiCall(Any::class.java) {
        apiService.addNewAddress( addressRequestBody.newAddressData)
    }


}