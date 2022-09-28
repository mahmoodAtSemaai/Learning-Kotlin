package com.webkul.mobikul.odoo.ui.price_comparison

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.mvicore.IModel
import com.webkul.mobikul.odoo.core.platform.BaseViewModel
import com.webkul.mobikul.odoo.core.utils.FailureStatus
import com.webkul.mobikul.odoo.core.utils.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.domain.enums.QuantityWarningType
import com.webkul.mobikul.odoo.domain.usecase.auth.IsUserLoggedInUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.AddToCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.BagItemsCountUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.CreateCartUseCase
import com.webkul.mobikul.odoo.domain.usecase.cart.GetCartIdUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.CheckChatFeatureEnabledUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.ConvertProductDetailsUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.GetProductDetailsUseCase
import com.webkul.mobikul.odoo.domain.usecase.product.GetProductSellersUseCase
import com.webkul.mobikul.odoo.domain.usecase.stock.HandleQuantityUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.AddItemToWishlistUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.IsWishListAllowedUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.ProductWishlistedUseCase
import com.webkul.mobikul.odoo.domain.usecase.wishlist.RemoveItemFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val checkChatFeatureEnabledUseCase: CheckChatFeatureEnabledUseCase,
    private val bagItemsCountUseCase: BagItemsCountUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val convertProductDetailsUseCase: ConvertProductDetailsUseCase,
    private val getCartIdUseCase: GetCartIdUseCase,
    private val createCartUseCase: CreateCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val addItemToWishlistUseCase: AddItemToWishlistUseCase,
    private val getProductSellersUseCase: GetProductSellersUseCase,
    private val handleQuantityUseCase: HandleQuantityUseCase,
    private val isWishListAllowedUseCase: IsWishListAllowedUseCase,
    private val removeItemFromWishlistUseCase: RemoveItemFromWishlistUseCase,
    private val firebaseAnalyticsImpl: FirebaseAnalyticsImpl,
    private val productWishlistedUseCase: ProductWishlistedUseCase
) : BaseViewModel(),
    IModel<ProductDetailsState, ProductDetailsIntent, ProductDetailsViewEffect> {

    override val intents: Channel<ProductDetailsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Idle)
    override val state: StateFlow<ProductDetailsState>
        get() = _state

    private val _effect = Channel<ProductDetailsViewEffect>()
    override val effect: Flow<ProductDetailsViewEffect>
        get() = _effect.receiveAsFlow()

    var currentProductId: String? = ""
    var productData: ProductEntity? = null
    var sellerProducts: ArrayList<ProductEntity> = ArrayList()
    var lastClickTime: Long = 0
    var isDescriptionExpanded: Boolean = false
    var cartId = -1

    init {
        handlerIntent()
    }


    override fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is ProductDetailsIntent.ReadMoreDescription -> readMoreDescription()
                    is ProductDetailsIntent.AddToCart -> isCartExists(
                        it.product,
                        it.quantity
                    )
                    is ProductDetailsIntent.WishlistBtnClicked -> wishlistBtnClicked()
                    is ProductDetailsIntent.CheckChatFeatureEnabled -> checkChatFeatureEnabled()
                    is ProductDetailsIntent.CheckOnNewIntent -> checkOnNewIntent()
                    is ProductDetailsIntent.GetBagItemsCount -> getBagItemsCount()
                    is ProductDetailsIntent.GetProductDetails -> getProductDetails(it.productTemplateId)
                    is ProductDetailsIntent.InitializeOnclickListeners -> initializeOnclickListeners()
                    is ProductDetailsIntent.InitializeStatusBarColor -> initializeStatusBarColor()
                    is ProductDetailsIntent.LogViewItemInFirebaseAnalytics -> logViewItemInFirebaseAnalytics(
                        it.productId,
                        it.productName
                    )
                    is ProductDetailsIntent.OpenSearchView -> openSearchView()
                    is ProductDetailsIntent.HandleProductQuantity -> handleProductQuantity(
                        it.currentQty,
                        it.data,
                        false,
                        null
                    )
                    is ProductDetailsIntent.ConvertProductDetails -> convertProductDetails(it.productData)
                    is ProductDetailsIntent.NavigateToBagActivity -> navigateToBagActivity()
                    is ProductDetailsIntent.NavigateToDrawerActivity -> navigateToDrawerActivity()
                    is ProductDetailsIntent.NavigateToProductReviewFragment -> {}//TODO()
                    is ProductDetailsIntent.ShareProduct -> shareProduct()
                    is ProductDetailsIntent.NavigateToChatActivity -> navigateToChatActivity(
                        it.sellerId,
                        it.sellerName,
                        it.sellerProfileImage
                    )
                    is ProductDetailsIntent.NavigateToSellerProfile -> navigateToSellerProfile(it.sellerId)
                    is ProductDetailsIntent.IsWishListAllowed -> isWishListAllowed()
                    // below are for seller products
                    is ProductDetailsIntent.GetSellerProducts -> getSellerProducts()
                    is ProductDetailsIntent.AddSellerProductToCart -> isCartExists(
                        it.product,
                        it.position
                    )
                    is ProductDetailsIntent.HandleProductQtyForSellerProduct -> handleProductQuantity(
                        it.currentQty,
                        it.product,
                        true,
                        it.position
                    )
                    is ProductDetailsIntent.BackPressed -> backPressed()
                    is ProductDetailsIntent.CheckIntentExtras -> checkIntentExtras(it.intent)
                }
            }
        }
    }

    private suspend fun backPressed() {
        _effect.send(ProductDetailsViewEffect.BackPressed)
    }

    private suspend fun wishlistBtnClicked() {
        when (val intent = productWishlistedUseCase(lastClickTime, productData!!)) {
            is Resource.Default -> {}
            is Resource.Success -> {
                lastClickTime = intent.value.lastClickTime
                when (intent.value.isWishlisted) {
                    true -> removeFromWishList()
                    false -> addToWishList()
                }
            }
        }
    }

    private suspend fun checkIntentExtras(intent: Intent?) {
        _effect.send(
            when (currentProductId) {
                "" -> ProductDetailsViewEffect.FirstTimeLaunch(intent)

                intent?.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_ID).toString() ->
                    ProductDetailsViewEffect.CloseSearchView

                else -> ProductDetailsViewEffect.ReloadProductActivity(intent)
            }
        )
    }

    private suspend fun readMoreDescription() {
        _effect.send(if (!isDescriptionExpanded) ProductDetailsViewEffect.ExpandedDescription else ProductDetailsViewEffect.CollapsedDescription)
        isDescriptionExpanded = !isDescriptionExpanded
    }

    private fun isWishListAllowed() {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {
                val productDetailsState: ProductDetailsState =
                    when (val intent = isWishListAllowedUseCase()) {
                        is Resource.Default -> ProductDetailsState.Loading
                        is Resource.Failure -> ProductDetailsState.HideWishListButton
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> {
                            if (intent.value) ProductDetailsState.ShowWishListButton
                            else ProductDetailsState.HideWishListButton
                        }
                    }
                productDetailsState
            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }


    private suspend fun navigateToSellerProfile(sellerId: Int?) {
        _effect.send(ProductDetailsViewEffect.NavigateToSellerProfile(sellerId))
    }

    private suspend fun navigateToChatActivity(
        sellerId: Int?,
        sellerName: String?,
        sellerProfileImage: String?
    ) {
        _effect.send(
            ProductDetailsViewEffect.NavigateToChatActivity(
                sellerId,
                sellerName,
                sellerProfileImage
            )
        )
    }


    private suspend fun shareProduct() {
        if (productData != null && !productData?.productId.isNullOrBlank()) {
            firebaseAnalyticsImpl.logShareEvent(productData!!.productId!!, productData?.name)
            _effect.send(ProductDetailsViewEffect.ShareProduct)
        } else _effect.send(ProductDetailsViewEffect.Error(FailureStatus.API_FAIL, null))
    }

    private suspend fun navigateToDrawerActivity() {
        _effect.send(ProductDetailsViewEffect.NavigateToDrawerActivity)
    }

    private suspend fun navigateToBagActivity() {
        _effect.send(ProductDetailsViewEffect.NavigateToBagActivity)
    }


    private fun removeFromWishList() {
        val isUserLoggedInUseCase = isUserLoggedInUseCase()
        if (isUserLoggedInUseCase is Resource.Success && isUserLoggedInUseCase.value) {
            viewModelScope.launch {

                _state.value = ProductDetailsState.Loading
                _state.value = try {
                    val intent = removeItemFromWishlistUseCase(productData?.productId)
                    var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                    intent.collect {
                        productDetailsState = when (it) {
                            is Resource.Default -> ProductDetailsState.Idle
                            is Resource.Failure -> ProductDetailsState.Error(
                                it.message,
                                it.failureStatus
                            )
                            is Resource.Loading -> ProductDetailsState.Loading
                            is Resource.Success -> {
                                productData!!.addedToWishlist = false
                                _effect.send(ProductDetailsViewEffect.RemovedItemFromWishList(it.value))
                                ProductDetailsState.Idle
                            }
                        }
                    }

                    productDetailsState
                } catch (e: Exception) {
                    ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
                }
            }
        } else _state.value = ProductDetailsState.ShowGuestUserLoginDialog

    }


    private fun addToWishList() {
        val isUserLoggedInUseCase = isUserLoggedInUseCase()
        if (isUserLoggedInUseCase is Resource.Success && isUserLoggedInUseCase.value) {
            viewModelScope.launch {

                _state.value = ProductDetailsState.Loading
                _state.value = try {

                    val intent =
                        addItemToWishlistUseCase(productData?.productId, productData?.name)
                    var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                    intent.collect {
                        productDetailsState = when (it) {
                            is Resource.Default -> ProductDetailsState.Idle
                            is Resource.Failure -> ProductDetailsState.Error(
                                it.message,
                                it.failureStatus
                            )
                            is Resource.Loading -> ProductDetailsState.Loading
                            is Resource.Success -> {
                                productData!!.addedToWishlist = true
                                _effect.send(ProductDetailsViewEffect.AddedItemToWishList(it.value))
                                ProductDetailsState.Idle
                            }
                        }
                    }

                    productDetailsState
                } catch (e: Exception) {
                    ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
                }
            }
        } else _state.value = ProductDetailsState.ShowGuestUserLoginDialog

    }

    private suspend fun isCartExists(product: ProductEntity?, position: Int? = null) {
        if (cartId == -1) {
            getCartId(
                product,
                position
            )
        } else {
            handleAddToCart(
                product,
                position
            )
        }
    }

    private fun getCartId(product: ProductEntity?, position: Int?) {
        viewModelScope.launch {
            val cartId = getCartIdUseCase()
            try {
                cartId.collect {
                    when (it) {
                        is Resource.Default -> _state.value = ProductDetailsState.Idle
                        is Resource.Failure -> createCart(product, position)
                        is Resource.Loading -> _state.value = ProductDetailsState.Loading
                        is Resource.Success -> {
                            this@ProductDetailsViewModel.cartId = it.value.cartId
                            handleAddToCart(product, position)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value =
                    ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun createCart(product: ProductEntity?, position: Int?) {
        viewModelScope.launch {
            val cartId = createCartUseCase()
            try {
                cartId.collect {
                    when (it) {
                        is Resource.Default -> _state.value = ProductDetailsState.Idle
                        is Resource.Failure -> ProductDetailsState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Loading -> _state.value = ProductDetailsState.Loading
                        is Resource.Success -> {
                            this@ProductDetailsViewModel.cartId = it.value.cartId
                            handleAddToCart(product, position)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value =
                    ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun handleAddToCart(
        product: ProductEntity?,
        position: Int?
    ) {
        val isSellerProduct = (product?.productId == productData!!.productId).not()
        try {
            handleQuantityUseCase(product?.getCurrentQuantity(), product).collect {
                when (it) {
                    is Resource.Failure -> ProductDetailsState.Error(it.message, it.failureStatus)
                    is Resource.Success -> {

                        when (it.value.warningType) {

                            QuantityWarningType.OUT_OF_STOCK.value -> {
                                if (isSellerProduct) _effect.send(
                                    ProductDetailsViewEffect.SetSellerProductQtyWithOutOfStockWarning(
                                        position!!,
                                        it.value.data
                                    )
                                )
                                else _effect.send(
                                    ProductDetailsViewEffect.SetQuantityWithOutOfStockWarning(
                                        it.value.data
                                    )
                                )
                            }

                            QuantityWarningType.LIMITED_STOCK.value -> {
                                if (isSellerProduct) _effect.send(
                                    ProductDetailsViewEffect.SetSellerProductQtyWithLimitedStockWarning(
                                        position!!,
                                        it.value.data
                                    )
                                )
                                else _effect.send(
                                    ProductDetailsViewEffect.SetQuantityWithLimitedStockWarning(
                                        it.value.data
                                    )
                                )
                            }

                            QuantityWarningType.NONE.value -> {
                                addToCart(product, position)
                            }

                        }
                    }
                }
            }
        } catch (e: Exception) {
            _effect.send(ProductDetailsViewEffect.Error(FailureStatus.API_FAIL, null))
        }
    }


    private fun addToCart(product: ProductEntity?, position: Int?) {
        val isUserLoggedInUseCase = isUserLoggedInUseCase()
        if (isUserLoggedInUseCase is Resource.Success && isUserLoggedInUseCase.value) {
            viewModelScope.launch {
                val intent =
                    addToCartUseCase(product?.productId, product?.getCurrentQuantity(), cartId)
                _state.value = ProductDetailsState.Loading
                try {
                    intent.collect {
                        _state.value = when (it) {
                            is Resource.Default -> ProductDetailsState.Idle
                            is Resource.Failure -> ProductDetailsState.Error(
                                it.message,
                                it.failureStatus
                            )
                            is Resource.Loading -> ProductDetailsState.Loading
                            is Resource.Success -> {
                                if (product!!.productId == productData!!.productId) _effect.send(
                                    ProductDetailsViewEffect.AddedItemToCart
                                )
                                else _effect.send(
                                    ProductDetailsViewEffect.AddedSellerProductToCart(
                                        position!!
                                    )
                                )
                                ProductDetailsState.Idle
                            }
                        }
                    }

                } catch (e: Exception) {
                    _state.value =
                        ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
                }
            }
        } else _state.value = ProductDetailsState.ShowGuestUserLoginDialog


    }

    private suspend fun handleProductQuantity(
        currentQty: Int?,
        data: ProductEntity?,
        isSellerProduct: Boolean,
        position: Int?
    ) {
        try {
            handleQuantityUseCase(currentQty, data).collect {
                when (it) {
                    is Resource.Failure -> ProductDetailsState.Error(it.message, it.failureStatus)
                    is Resource.Success -> {

                        when (it.value.warningType) {

                            QuantityWarningType.OUT_OF_STOCK.value -> {
                                if (isSellerProduct) _effect.send(
                                    ProductDetailsViewEffect.SetSellerProductQtyWithOutOfStockWarning(
                                        position!!,
                                        it.value.data
                                    )
                                )
                                else _effect.send(
                                    ProductDetailsViewEffect.SetQuantityWithOutOfStockWarning(
                                        it.value.data
                                    )
                                )
                            }

                            QuantityWarningType.LIMITED_STOCK.value -> {
                                if (isSellerProduct) _effect.send(
                                    ProductDetailsViewEffect.SetSellerProductQtyWithLimitedStockWarning(
                                        position!!,
                                        it.value.data
                                    )
                                )
                                else _effect.send(
                                    ProductDetailsViewEffect.SetQuantityWithLimitedStockWarning(
                                        it.value.data
                                    )
                                )
                            }

                            QuantityWarningType.NONE.value -> {
                                if (isSellerProduct) _effect.send(
                                    ProductDetailsViewEffect.SetSellerProductQuantity(
                                        position!!,
                                        it.value.data
                                    )
                                )
                                else _effect.send(ProductDetailsViewEffect.SetQuantity(it.value.data))
                            }

                        }
                    }
                }
            }
        } catch (e: Exception) {
            _effect.send(ProductDetailsViewEffect.Error(FailureStatus.API_FAIL, null))
        }
    }


    private suspend fun openSearchView() {
        _effect.send(ProductDetailsViewEffect.OpenSearchView)
    }

    private fun logViewItemInFirebaseAnalytics(productId: String?, productName: String?) {
        firebaseAnalyticsImpl.logViewItem(productId, productName)
        _state.value = ProductDetailsState.LoggedViewItemInFirebaseAnalytics
    }

    private fun initializeStatusBarColor() {
        _state.value = ProductDetailsState.SetStatusBarColor

    }

    private fun initializeOnclickListeners() {
        _state.value = ProductDetailsState.SetOnclickListeners
    }

    private fun getBagItemsCount() {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {
                var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                bagItemsCountUseCase().collect {
                    productDetailsState = when (it) {
                        is Resource.Default -> ProductDetailsState.HideBagItemCount
                        is Resource.Failure -> ProductDetailsState.Error(
                            it.message, it.failureStatus
                        )
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> ProductDetailsState.ShowBagItemCount(it.value)

                    }
                }

                productDetailsState
            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private suspend fun checkOnNewIntent() {
        _effect.send(ProductDetailsViewEffect.CheckOnNewIntent)
    }

    private fun checkChatFeatureEnabled() {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {
                var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                checkChatFeatureEnabledUseCase().collect {
                    productDetailsState = when (it) {
                        is Resource.Default -> ProductDetailsState.Idle
                        is Resource.Failure -> ProductDetailsState.HideChatButton
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> if (it.value) ProductDetailsState.ShowChatButton else ProductDetailsState.HideChatButton
                    }
                }

                productDetailsState
            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }

    private fun getProductDetails(productTemplateId: Int?) {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {
                var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                getProductDetailsUseCase(productTemplateId).collect {
                    productDetailsState = when (it) {
                        is Resource.Default -> ProductDetailsState.Idle
                        is Resource.Failure -> ProductDetailsState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> ProductDetailsState.FetchedProductDetails(it.value)
                    }
                }

                productDetailsState
            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }
        }
    }

    private fun convertProductDetails(productData: ProductEntity) {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {
                var productDetailsState: ProductDetailsState = ProductDetailsState.Idle

                convertProductDetailsUseCase(productData).collect {
                    productDetailsState = when (it) {
                        is Resource.Default -> ProductDetailsState.Idle
                        is Resource.Failure -> ProductDetailsState.Error(
                            it.message,
                            it.failureStatus
                        )
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> ProductDetailsState.ConvertedProductDetails(it.value)
                    }
                }

                productDetailsState
            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }


    /// below is for seller products

    private fun getSellerProducts() {
        viewModelScope.launch {
            _state.value = ProductDetailsState.Loading
            _state.value = try {

                var productDetailsState: ProductDetailsState = ProductDetailsState.Idle
                val intent = getProductSellersUseCase(
                    productData?.globalProductId?.toInt(),
                    productData?.templateId
                )
                intent.collect {

                    productDetailsState = when (it) {
                        is Resource.Default -> ProductDetailsState.Loading
                        is Resource.Failure -> ProductDetailsState.Error(
                            it.message,
                            FailureStatus.OTHER
                        )
                        is Resource.Loading -> ProductDetailsState.Loading
                        is Resource.Success -> ProductDetailsState.SetSellerProducts(it.value)
                    }
                }
                productDetailsState

            } catch (e: Exception) {
                ProductDetailsState.Error(e.localizedMessage, FailureStatus.OTHER)
            }

        }
    }


}