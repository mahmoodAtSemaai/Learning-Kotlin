package com.webkul.mobikul.odoo.ui.checkout.address

import android.os.Bundle
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.*
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody

sealed class UpdateAddressIntent : IIntent {

    data class EditAddress(val url: String,
                           val addressRequestBody: AddressRequestBody
    ) : UpdateAddressIntent()
    data class AddAddress( val addressRequestBody: AddressRequestBody) : UpdateAddressIntent()
    data class BundleData(val bundle: Bundle?) : UpdateAddressIntent()
    data class FetchAddressData(val url: String) : UpdateAddressIntent()
    data class FetchStates(val companyId: Int) : UpdateAddressIntent()
    data class FetchDistricts(val stateId: Int) : UpdateAddressIntent()
    data class FetchSubDistricts(val districtId: Int) : UpdateAddressIntent()
    data class FetchVillages(val subDistrictId: Int) : UpdateAddressIntent()
    data class ValidateMandatoryFields(val isServiceAvailable: Boolean,
                                       val addressRequestBody: AddressRequestBody) : UpdateAddressIntent()
    data class SelectState(val stateEntity: StateEntity) : UpdateAddressIntent()
    data class SelectDistrict(val districtEntity: DistrictEntity) : UpdateAddressIntent()
    data class SelectSubDistrict(val subDistrictEntity: SubDistrictEntity) : UpdateAddressIntent()
    data class SelectVillage(val villageEntity: VillageEntity) : UpdateAddressIntent()
    object ServiceNotAvailable : UpdateAddressIntent()

}