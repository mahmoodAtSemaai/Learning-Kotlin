package com.webkul.mobikul.odoo.ui.seller

import android.view.ViewGroup
import com.webkul.mobikul.odoo.core.mvicore.IIntent

sealed class SellerProfileIntent : IIntent {
	object OpenSearch : SellerProfileIntent()
	object CloseSearch : SellerProfileIntent()
	object SetActionBar : SellerProfileIntent()
	object IsChatFeatureEnabled : SellerProfileIntent()
	object IsWishlistAllowed : SellerProfileIntent()
	object InitOnclickListeners : SellerProfileIntent()
	data class CheckActivityResult(val requestCode : Int, val resultCode : Int) : SellerProfileIntent()
	
	
	data class ReadMore(val params : ViewGroup.LayoutParams) : SellerProfileIntent()
	
	data class ViewSellerPolicies(val returnPolicy : String?, val shippingPolicy : String?) : SellerProfileIntent()
	
	data class ViewSellerReviews(val sellerId : Int?, val size : Int?) : SellerProfileIntent()
	
	
	data class NavigateToChatActivity(val sellerId : Int?, val sellerName : String?, val sellerProfileImage : String?) : SellerProfileIntent()
	
	data class GetSellerProfileData(val sellerId : Int?) : SellerProfileIntent()
	data class GetSellerProducts(val sellerId : Int?) : SellerProfileIntent()
	
	object NavigateToWishList : SellerProfileIntent()
	
	data class ViewSellerCollections(val sellerId : Int?) : SellerProfileIntent()
	
}