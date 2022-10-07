package com.webkul.mobikul.odoo.domain.repository

import com.webkul.mobikul.odoo.core.data.Repository
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody
import com.webkul.mobikul.odoo.data.entity.AddressEntity
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest

interface AddressRepository : Repository<AddressEntity, Any, BaseLazyRequest> {

    suspend fun updateAddressForOrder(orderId: Int, updateOrderRequest: UpdateOrderRequest?) : Resource<Any>

    suspend fun deleteAddress(url: String) : Resource<DeleteAddressEntity>

    suspend fun getAddressFormData(url: String): Resource<AddressFormEntity>

    suspend fun getStates(companyId: Int): Resource<StateListEntity>

    suspend fun getDistricts(stateId: Int): Resource<DistrictListEntity>

    suspend fun getSubDistricts(districtId: Int): Resource<SubDistrictListEntity>

    suspend fun getVillages(subDistrictId: Int): Resource<VillageListEntity>

    suspend fun editAddress(
        url: String,
        addressRequestBody: AddressRequestBody
    ): Resource<Any>

    suspend fun addAddress(addressRequestBody: AddressRequestBody): Resource<Any>
}