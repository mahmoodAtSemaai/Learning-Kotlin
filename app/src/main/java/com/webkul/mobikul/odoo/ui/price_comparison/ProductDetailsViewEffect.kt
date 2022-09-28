package com.webkul.mobikul.odoo.ui.price_comparison

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.ProductEntity

sealed class ProductDetailsViewEffect : IEffect {
	
	object ShareProduct : ProductDetailsViewEffect()
	object OpenSearchView : ProductDetailsViewEffect()
	object AddedItemToCart : ProductDetailsViewEffect()
	object ExpandedDescription : ProductDetailsViewEffect()
	object CollapsedDescription : ProductDetailsViewEffect()
	object NavigateToBagActivity : ProductDetailsViewEffect()
	object NavigateToDrawerActivity : ProductDetailsViewEffect()
	object CheckOnNewIntent : ProductDetailsViewEffect()
	object BackPressed : ProductDetailsViewEffect()
	object CloseSearchView : ProductDetailsViewEffect()
	data class FirstTimeLaunch(val intent : Intent?) : ProductDetailsViewEffect()
	data class ReloadProductActivity(val intent : Intent?) : ProductDetailsViewEffect()
	
	
	data class AddedSellerProductToCart(val position : Int) : ProductDetailsViewEffect()
	data class SetQuantity(val product : ProductEntity) : ProductDetailsViewEffect()
	data class AddedItemToWishList(val message : String?) : ProductDetailsViewEffect()
	data class NavigateToSellerProfile(val sellerId : Int?) : ProductDetailsViewEffect()
	data class RemovedItemFromWishList(val message : String?) : ProductDetailsViewEffect()
	data class ShowDialog(val title : String, val message : String) : ProductDetailsViewEffect()
	data class SetQuantityWithOutOfStockWarning(val product : ProductEntity) : ProductDetailsViewEffect()
	data class SetQuantityWithLimitedStockWarning(val product : ProductEntity) : ProductDetailsViewEffect()
	data class Error(val failureStatus : FailureStatus, val message : String?) : ProductDetailsViewEffect()
	
	
	data class SetSellerProductQuantity(
			val position : Int,
			var product : ProductEntity
	) : ProductDetailsViewEffect()
	
	data class SetSellerProductQtyWithOutOfStockWarning(
			val position : Int,
			var product : ProductEntity
	) : ProductDetailsViewEffect()
	
	data class SetSellerProductQtyWithLimitedStockWarning(
			val position : Int,
			var product : ProductEntity
	) : ProductDetailsViewEffect()
	
	data class NavigateToChatActivity(
			val sellerId : Int?,
			val sellerName : String?,
			val sellerProfileImage : String?,
	) : ProductDetailsViewEffect()
	
}