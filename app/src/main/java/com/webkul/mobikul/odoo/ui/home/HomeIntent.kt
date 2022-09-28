package com.webkul.mobikul.odoo.ui.home

import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.AddressFormEntity
import com.webkul.mobikul.odoo.data.entity.BannerEntity
import com.webkul.mobikul.odoo.data.entity.StateListEntity
import com.webkul.mobikul.odoo.domain.enums.HomeRefreshState
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class HomeIntent : IIntent {

    object InitView : HomeIntent()
    object GetBannerData : HomeIntent()
    object GetBillingAddress : HomeIntent()
    object UpdateUserDetails : HomeIntent()
    object SetOnclickListeners : HomeIntent()
    object GetProductCategories : HomeIntent()
    object BackPressed : HomeIntent()
    object GetLoyaltyPoints : HomeIntent()

    data class OnBannerClick(val bannerEntity: BannerEntity) : HomeIntent()
    data class GetAddressFormData(val addressData: AddressData) : HomeIntent()
    data class SelectCategory(val position: Int, val previousPosition: Int) : HomeIntent()
    data class GetStates(
        val companyId: Int,
        val addressFormEntity: AddressFormEntity,
        val addressData: AddressData
    ) : HomeIntent()

    data class CheckStateAvailability(
        val stateListEntity: StateListEntity,
        val addressFormEntity: AddressFormEntity,
        val addressData: AddressData
    ) : HomeIntent()

    data class RefreshStateChanged(val homeRefreshState: HomeRefreshState) : HomeIntent()
}
