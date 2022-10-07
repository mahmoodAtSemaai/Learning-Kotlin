package com.webkul.mobikul.odoo.ui.checkout.address

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody

sealed class UpdateAddressState : IState {

    object Idle : UpdateAddressState()
    object Loading : UpdateAddressState()
    data class WarningSnackBar(val message: String?) : UpdateAddressState()
    data class Toast(val message: String?) : UpdateAddressState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : UpdateAddressState()
    data class UpdateError(val message: String?, val failureStatus: FailureStatus, val code: Int?) : UpdateAddressState()
    object EditAddress : UpdateAddressState()
    object AddAddress : UpdateAddressState()
    data class OnSuccessfulValidateFields(val addressRequestBody: AddressRequestBody) : UpdateAddressState()
    data class FetchAddressFormData(val addressFormEntity: AddressFormEntity) : UpdateAddressState()
    data class FetchStates(val stateList: StateListEntity) : UpdateAddressState()
    data class FetchDistricts(val districtList: DistrictListEntity) : UpdateAddressState()
    data class FetchSubDistricts(val subDistrictList: SubDistrictListEntity) : UpdateAddressState()
    data class FetchVillages(val villageList: VillageListEntity) : UpdateAddressState()
    data class StateSelected(val stateEntity: StateEntity) : UpdateAddressState()
    data class DistrictSelected(val districtEntity: DistrictEntity) : UpdateAddressState()
    data class SubDistrictSelected(val subDistrictEntity: SubDistrictEntity) : UpdateAddressState()
    data class VillageSelected(val villageEntity: VillageEntity) : UpdateAddressState()
    object ServiceNotAvailable : UpdateAddressState()

}
