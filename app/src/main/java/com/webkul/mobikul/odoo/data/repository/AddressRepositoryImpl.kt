package com.webkul.mobikul.odoo.data.repository

import com.webkul.mobikul.odoo.core.data.BaseRepository
import com.webkul.mobikul.odoo.core.data.IDataSource
import com.webkul.mobikul.odoo.core.data.ModelEntityParser
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.data.remoteSource.remoteDataSource.AddressRemoteDataSource
import com.webkul.mobikul.odoo.domain.repository.AddressRepository
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
        private val remoteDataSource: AddressRemoteDataSource
) : AddressRepository, BaseRepository<AddressEntity, Any, BaseLazyRequest, MyAddressesResponse>() {

    override val entityParser
        get() = ModelEntityParser(AddressEntity::class.java, MyAddressesResponse::class.java)

    override var dataSource: IDataSource<MyAddressesResponse, Any, BaseLazyRequest> = remoteDataSource

    override suspend fun get(request: BaseLazyRequest): Resource<AddressEntity> {
        return super<BaseRepository>.get(request)
    }
    override suspend fun updateAddressForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) : Resource<Any> {
        return remoteDataSource.updateAddressForOrder(orderId, updateOrderRequest)
    }

    override suspend fun deleteAddress(url: String) : Resource<DeleteAddressEntity> {
        return remoteDataSource.deleteAddress(url)
    }

    override suspend fun getAddressFormData(url: String) : Resource<AddressFormEntity> {
        return remoteDataSource.getAddressFormData(url)
    }

    override suspend fun getStates(companyId: Int) : Resource<StateListEntity> {
        return remoteDataSource.getStates(companyId)
    }

    override suspend fun getDistricts(stateId: Int) : Resource<DistrictListEntity> {
        return remoteDataSource.getDistricts(stateId)
	}

    override suspend fun getSubDistricts(districtId: Int) : Resource<SubDistrictListEntity> {
        return remoteDataSource.getSubDistricts(districtId)
    }

    override suspend fun getVillages(subDistrictId: Int) : Resource<VillageListEntity> {
        return remoteDataSource.getVillages(subDistrictId)
    }

    override suspend fun editAddress( url: String, addressRequestBody: AddressRequestBody) : Resource<Any> {
        return remoteDataSource.editAddress(url, addressRequestBody)
    }

    override suspend fun addAddress(addressRequestBody: AddressRequestBody) : Resource<Any> {
        return remoteDataSource.addAddress( addressRequestBody)
    }
}