package com.webkul.mobikul.odoo.ui.signUpOnboarding.effect

import com.webkul.mobikul.odoo.core.mvicore.IEffect

sealed class UserAddressEffect :IEffect {
    data class ExpandSpinner(val spinnerType : Int) : UserAddressEffect()
    data class SelectedProvince(val name : String) : UserAddressEffect()
    data class UnavailableProvince(val name : String) : UserAddressEffect()
    object LocationDialog: UserAddressEffect()
    data class ResetDistrictSpinner(val provinceId : Int) : UserAddressEffect()
    data class ResetSubDistrictSpinner(val districtId : Int) : UserAddressEffect()
    data class ResetVillageSpinner(val subDistrictId : Int) : UserAddressEffect()
    data class EnableSpinner(val spinnerType:Int, val enable: Boolean) : UserAddressEffect()
}