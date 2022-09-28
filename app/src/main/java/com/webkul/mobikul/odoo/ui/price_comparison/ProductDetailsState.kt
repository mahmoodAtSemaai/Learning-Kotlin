package com.webkul.mobikul.odoo.ui.price_comparison

import com.webkul.mobikul.odoo.core.mvicore.IState
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductListEntity

sealed class ProductDetailsState : IState {
	
	object Idle : ProductDetailsState()
	object Loading : ProductDetailsState()
	object ShowChatButton : ProductDetailsState()
	object HideChatButton : ProductDetailsState()
	object HideBagItemCount : ProductDetailsState()
	object SetStatusBarColor : ProductDetailsState()
	object ShowWishListButton : ProductDetailsState()
	object HideWishListButton : ProductDetailsState()
	object SetOnclickListeners : ProductDetailsState()
	object ShowGuestUserLoginDialog : ProductDetailsState()
	object LoggedViewItemInFirebaseAnalytics : ProductDetailsState()
	data class ShowBagItemCount(val count : String) : ProductDetailsState()
	data class FetchedProductDetails(val data : ProductEntity) : ProductDetailsState()
	data class SetSellerProducts(val productList : ProductListEntity) : ProductDetailsState()
	data class Error(val message : String?, val failureStatus : FailureStatus) : ProductDetailsState()
	data class ConvertedProductDetails(val productDetails : MutableMap<String, List<String>>) : ProductDetailsState()
	
}