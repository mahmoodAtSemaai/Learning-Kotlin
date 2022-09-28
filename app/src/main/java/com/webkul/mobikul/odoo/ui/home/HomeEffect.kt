package com.webkul.mobikul.odoo.ui.home

import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.BannerEntity
import com.webkul.mobikul.odoo.data.entity.BannerListEntity
import com.webkul.mobikul.odoo.data.entity.ProductCategoriesEntity
import com.webkul.mobikul.odoo.model.customer.address.AddressData

sealed class HomeEffect : IEffect {

    data class ShowPromptToCompleteBillingAddress(val addressData: AddressData) : HomeEffect()
    data class Error(val msg: String?, val failureStatus: FailureStatus) : HomeEffect()

    data class ProductCategoriesSuccess(val categories: ProductCategoriesEntity) : HomeEffect()
    data class BannerDataSuccess(val value: BannerListEntity) : HomeEffect()
    data class SelectCategory(val position: Int, val previousPosition: Int) : HomeEffect()

    object SetOnclickListeners : HomeEffect()
    data class OnBannerClick(val bannerEntity: BannerEntity) : HomeEffect()


    object ShowBannerShimmer : HomeEffect()
    object ShowCategoriesShimmer : HomeEffect()
    object BackPressed : HomeEffect()
    object GetLoyaltyPoints : HomeEffect()

    object HideBannerShimmer : HomeEffect()
    object HideCategoriesShimmer : HomeEffect()

    object UpdateCartCount : HomeEffect()
    object InitView : HomeEffect()
}
