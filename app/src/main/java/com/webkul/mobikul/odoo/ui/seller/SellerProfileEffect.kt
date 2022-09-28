package com.webkul.mobikul.odoo.ui.seller

import android.view.ViewGroup
import com.webkul.mobikul.odoo.core.mvicore.IEffect
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.data.entity.SellerDetailsEntity

sealed class SellerProfileEffect : IEffect {
	
	object OpenSearch : SellerProfileEffect()
	object CloseSearch : SellerProfileEffect()
	
	object ShowWishlistButton : SellerProfileEffect()
	object HideWishlistButton : SellerProfileEffect()
	
	object RecreateActivity: SellerProfileEffect()
	data class ReadMore(val params : ViewGroup.LayoutParams) : SellerProfileEffect()
	data class ReadLess(val params : ViewGroup.LayoutParams) : SellerProfileEffect()
	
	data class ViewSellerCollections(val sellerId : Int?) : SellerProfileEffect()
	data class ViewSellerPolicies(val returnPolicy : String, val shippingPolicy : String) : SellerProfileEffect()
	
	data class ViewSellerReviews(val sellerId : Int, val size : Int) : SellerProfileEffect()
	
	data class NavigateToChatActivity(val sellerId : Int, val sellerName : String, val sellerProfileImage : String) : SellerProfileEffect()
	object NavigateToWishList : SellerProfileEffect()
	
	data class FetchedSellerDetails(val data : SellerDetailsEntity) : SellerProfileEffect()
	data class FetchedSellerProducts(val data : ProductListEntity) : SellerProfileEffect()
	data class Error(val failureStatus : FailureStatus, val message : String? ) : SellerProfileEffect()
	
}
