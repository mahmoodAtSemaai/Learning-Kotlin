package com.webkul.mobikul.odoo.ui.signUpOnboarding.intent

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.data.request.UserAddressRequest
import com.webkul.mobikul.odoo.data.request.UserLocationRequest
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.DistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.ProvinceSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.SubDistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.VillageSpinnerAdapter

sealed class UserAddressIntent :IIntent {
    object GetHomePageData : UserAddressIntent()
    object GetCustomerId : UserAddressIntent()
    data class GetArgs(val arguments : Bundle?): UserAddressIntent()
    data class Continue(val userAddressRequest: UserAddressRequest) : UserAddressIntent()
    data class LocationContinue(val userLocationRequest: UserLocationRequest) : UserAddressIntent()
    data class ExpandSpinner(val spinner : View, val dropDown:ImageView,val type : Int) : UserAddressIntent()
    data class VerifyFields(val userAddressRequest: UserAddressRequest) : UserAddressIntent()

    data class ProvinceSpinner(val companyId:Int) : UserAddressIntent()
    data class ProvinceOnItemClick(val position: Int, val addressList: List<StateEntity>) : UserAddressIntent()
    data class ProvinceSearch(val query: String, val addressList: List<StateEntity>, val adapter: ProvinceSpinnerAdapter,val spinnerType : Int) : UserAddressIntent()

    data class DistrictSpinner(val stateId:Int) : UserAddressIntent()
    data class DistrictOnItemClick(val position: Int, val addressList: List<DistrictEntity>) : UserAddressIntent()
    data class DistrictSearch(val query: String, val addressList: List<DistrictEntity>, val adapter: DistrictSpinnerAdapter,val spinnerType : Int) : UserAddressIntent()

    data class SubDistrictSpinner(val districtId:Int) : UserAddressIntent()
    data class SubDistrictOnItemClick(val position: Int, val addressList: List<SubDistrictEntity>) : UserAddressIntent()
    data class SubDistrictSearch(val query: String, val addressList: List<SubDistrictEntity>, val adapter: SubDistrictSpinnerAdapter,val spinnerType : Int) : UserAddressIntent()

    data class VillageSpinner(val subDistrictId:Int) : UserAddressIntent()
    data class VillageOnItemClick(val position: Int, val addressList: List<VillageEntity>) : UserAddressIntent()
    data class VillageSearch(val query: String, val addressList: List<VillageEntity>, val adapter: VillageSpinnerAdapter,val spinnerType : Int) : UserAddressIntent()

    object ResetDistrictSpinner : UserAddressIntent()
    object ResetSubDistrictSpinner : UserAddressIntent()
    object ResetVillageSpinner : UserAddressIntent()
    data class SpinnerState(val spinnerType: Int) : UserAddressIntent()
    object CheckPendingStage : UserAddressIntent()
    object CloseApp : UserAddressIntent()

}