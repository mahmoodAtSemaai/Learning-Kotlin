package com.webkul.mobikul.odoo.ui.price_comparison

import android.content.Intent
import com.webkul.mobikul.odoo.core.mvicore.IIntent
import com.webkul.mobikul.odoo.data.entity.ProductEntity

sealed class ProductDetailsIntent : IIntent {

    data class LogViewItemInFirebaseAnalytics(val productId: String?, val productName: String?) :
        ProductDetailsIntent()

    data class CheckIntentExtras(val intent: Intent?) : ProductDetailsIntent()

    object CheckOnNewIntent : ProductDetailsIntent()
    object BackPressed : ProductDetailsIntent()

    object CheckChatFeatureEnabled : ProductDetailsIntent()
    object InitializeOnclickListeners : ProductDetailsIntent()
    object GetBagItemsCount : ProductDetailsIntent()
    object InitializeStatusBarColor : ProductDetailsIntent()

    data class ConvertProductDetails(val productData: ProductEntity) : ProductDetailsIntent()

    data class GetProductDetails(val productTemplateId: Int?) : ProductDetailsIntent()

    object ReadMoreDescription : ProductDetailsIntent()

    object NavigateToBagActivity : ProductDetailsIntent()
    object NavigateToDrawerActivity : ProductDetailsIntent()
    object NavigateToProductReviewFragment : ProductDetailsIntent()

    object OpenSearchView : ProductDetailsIntent()

    data class HandleProductQuantity(val currentQty: Int?, var data: ProductEntity?) :
        ProductDetailsIntent()

    object IsWishListAllowed : ProductDetailsIntent()


    object WishlistBtnClicked : ProductDetailsIntent()
    object ShareProduct : ProductDetailsIntent()

    data class NavigateToChatActivity(
        val sellerId: Int?,
        val sellerName: String?,
        val sellerProfileImage: String?
    ) : ProductDetailsIntent()

    data class AddToCart(val product: ProductEntity?, val quantity: Int?) : ProductDetailsIntent()
    data class NavigateToSellerProfile(val sellerId: Int?) : ProductDetailsIntent()

    // for seller products

    object GetSellerProducts : ProductDetailsIntent()

    data class AddSellerProductToCart(val product: ProductEntity?, val position: Int) :
        ProductDetailsIntent()

    data class HandleProductQtyForSellerProduct(
        val currentQty: Int,
        var product: ProductEntity,
        val position: Int
    ) : ProductDetailsIntent()


}