package com.webkul.mobikul.odoo.ui.signUpOnboarding.state

import android.view.View
import android.widget.ImageView
import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.address.DistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.StateEntity
import com.webkul.mobikul.odoo.data.entity.address.SubDistrictEntity
import com.webkul.mobikul.odoo.data.entity.address.VillageEntity
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.DistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.ProvinceSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.SubDistrictSpinnerAdapter
import com.webkul.mobikul.odoo.ui.signUpOnboarding.adapter.VillageSpinnerAdapter

sealed class UserAddressState : IState {
    object Idle : UserAddressState()
    object Loading : UserAddressState()
    object FetchProvinceData : UserAddressState()
    object GetArgs : UserAddressState()
    object ShowLocationDialog : UserAddressState()
    object GetHomeData : UserAddressState()
    object CompletedAddress : UserAddressState()
    data class ContinueBtn(val enable:Boolean) : UserAddressState()
    data class ExpandSpinner(val spinner : View, val dropDown: ImageView) : UserAddressState()
    data class Error(val message: String?, val failureStatus: FailureStatus) : UserAddressState()

    data class GetProvinceData(val provinceData:List<StateEntity>) : UserAddressState()
    data class ProvinceSearchResult(val adapter: ProvinceSpinnerAdapter, val provinceData:List<StateEntity>) : UserAddressState()
    data class AvailableProvince(val provinceName: String,val provinceId:Int) : UserAddressState()
    data class UnavailableProvince(val provinceName: String) : UserAddressState()

    data class GetDistrictData(val districtData:List<DistrictEntity>) : UserAddressState()
    data class SelectedDistrict(val districtName: String,val districtId:Int) : UserAddressState()
    data class DistrictSearchResult(val adapter: DistrictSpinnerAdapter, val districtData:List<DistrictEntity>) : UserAddressState()

    data class GetSubDistrictData(val subDistrictData:List<SubDistrictEntity>) : UserAddressState()
    data class SelectedSubDistrict(val subDistrictName: String,val subDistrictId:Int) : UserAddressState()
    data class SubDistrictSearchResult(val adapter: SubDistrictSpinnerAdapter, val subDistrictData:List<SubDistrictEntity>) : UserAddressState()

    data class GetVillageData(val villageData:List<VillageEntity>) : UserAddressState()
    data class SelectedVillage(val villageName: String,val postalCode:String) : UserAddressState()
    data class VillageSearchResult(val adapter: VillageSpinnerAdapter, val villageData:List<VillageEntity>) : UserAddressState()

    data class EmptySearch(val spinnerType:Int) : UserAddressState()
    object CloseApp : UserAddressState()
}