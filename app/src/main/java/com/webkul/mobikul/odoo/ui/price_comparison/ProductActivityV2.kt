package com.webkul.mobikul.odoo.ui.price_comparison

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.*
import com.webkul.mobikul.odoo.adapter.product.ProductDetailsAdapterV1
import com.webkul.mobikul.odoo.adapter.product.ProductImageAdapter
import com.webkul.mobikul.odoo.constant.ApplicationConstant.SET_QTY_ERROR_DIALOG_TIME
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.isVisibile
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeInvisible
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.mvicore.IView
import com.webkul.mobikul.odoo.core.platform.BindingBaseActivity
import com.webkul.mobikul.odoo.custom.CustomToast
import com.webkul.mobikul.odoo.data.entity.ProductEntity
import com.webkul.mobikul.odoo.data.entity.ProductListEntity
import com.webkul.mobikul.odoo.databinding.ActivityProductV2Binding
import com.webkul.mobikul.odoo.helper.ImageHelper
import com.webkul.mobikul.odoo.ui.cart.NewCartActivity
import com.webkul.mobikul.odoo.ui.seller.SellerProfileActivityV1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductActivityV2 @Inject constructor() :
    BindingBaseActivity<ActivityProductV2Binding>(),
    IView<ProductDetailsIntent, ProductDetailsState, ProductDetailsViewEffect>,
    ProductSellersListener {

    override val contentViewId: Int = R.layout.activity_product_v2
    private val viewModel: ProductDetailsViewModel by viewModels()

    private lateinit var productSellersAdapter: ProductSellersAdapter
    private var descriptionLineCount: Int = 0
    private var isFirstTimeLaunch = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        startInit()
    }

    override fun onResume() {
        super.onResume()
        getBagItemsCount()
        getProductData()
    }

    override fun onNewIntent(intent: Intent?) {
        checkIntentExtras(intent)
        super.onNewIntent(intent)
    }


    private fun startInit() {
        triggerIntent(
            ProductDetailsIntent.LogViewItemInFirebaseAnalytics(
                intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_ID),
                intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_NAME)
            )
        )
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }

        lifecycleScope.launch {
            viewModel.effect.collect {
                render(it)
            }
        }
    }

    override fun render(state: ProductDetailsState) {
        when (state) {
            is ProductDetailsState.Idle -> {}
            is ProductDetailsState.Loading -> showProgressDialog()
            is ProductDetailsState.LoggedViewItemInFirebaseAnalytics -> setBackButtonInToolbar()
            is ProductDetailsState.HideChatButton -> hideChatButton()
            is ProductDetailsState.ShowChatButton -> showChatButton()
            is ProductDetailsState.SetOnclickListeners -> setOnClickListeners()
            is ProductDetailsState.SetStatusBarColor -> setStatusBarColor()
            is ProductDetailsState.ShowBagItemCount -> showBagItemCount(state.count)
            is ProductDetailsState.HideBagItemCount -> hideBagItemCount()
            is ProductDetailsState.FetchedProductDetails -> fetchedProductDetails(state.data)
            is ProductDetailsState.ConvertedProductDetails -> setProductDetails(state.productDetails)

            is ProductDetailsState.HideWishListButton -> {
                binding.btnWishlist.makeGone()
            }

            is ProductDetailsState.ShowWishListButton -> {
                binding.btnWishlist.makeVisible()
            }

            is ProductDetailsState.Error -> {
                dismissProgressDialog()
                showErrorState(state.failureStatus, state.message)
            }

            is ProductDetailsState.ShowGuestUserLoginDialog -> showGuestUserLoginDialog()

            is ProductDetailsState.SetSellerProducts -> setSellerProducts(state.productList)

        }
    }


    override fun render(effect: ProductDetailsViewEffect) {
        when (effect) {
            is ProductDetailsViewEffect.SetQuantity -> setQuantity(effect.product)

            is ProductDetailsViewEffect.SetQuantityWithLimitedStockWarning ->
                setQuantityWithWarning(
                    getString(R.string.product_quantity_exceeding),
                    effect.product
                )

            is ProductDetailsViewEffect.SetQuantityWithOutOfStockWarning ->
                setQuantityWithWarning(getString(R.string.product_out_of_stock), effect.product)

            is ProductDetailsViewEffect.NavigateToBagActivity -> navigateToBagActivity()

            is ProductDetailsViewEffect.NavigateToDrawerActivity -> navigateToDrawerActivity()

            is ProductDetailsViewEffect.ShowDialog -> showErrorDialog(effect.title, effect.message)

            is ProductDetailsViewEffect.ShareProduct -> shareProduct()

            is ProductDetailsViewEffect.NavigateToChatActivity ->
                navigateToChatActivity(
                    effect.sellerId,
                    effect.sellerName,
                    effect.sellerProfileImage
                )

            is ProductDetailsViewEffect.NavigateToSellerProfile -> navigateToSellerProfile(effect.sellerId)

            is ProductDetailsViewEffect.AddedItemToWishList -> addedItemToWishList(effect.message)

            is ProductDetailsViewEffect.RemovedItemFromWishList -> removedItemToWishList(effect.message)

            is ProductDetailsViewEffect.CollapsedDescription -> collapseDescription()

            is ProductDetailsViewEffect.ExpandedDescription -> expandDescription()

            is ProductDetailsViewEffect.AddedItemToCart -> addedItemToCart()

            is ProductDetailsViewEffect.OpenSearchView -> openSearchView()

            is ProductDetailsViewEffect.CheckOnNewIntent -> onNewIntent(intent)

            is ProductDetailsViewEffect.Error -> showErrorState(
                effect.failureStatus,
                effect.message
            )

            is ProductDetailsViewEffect.BackPressed -> backPressed()

            is ProductDetailsViewEffect.CloseSearchView -> closeSearchView()
            is ProductDetailsViewEffect.FirstTimeLaunch -> handleFirstTimeLaunch(effect.intent)
            is ProductDetailsViewEffect.ReloadProductActivity -> reloadProductActivity(effect.intent)


            // below effects are for seller products

            is ProductDetailsViewEffect.SetSellerProductQuantity -> setSellerProductQuantity(
                effect.position,
                effect.product
            )

            is ProductDetailsViewEffect.SetSellerProductQtyWithLimitedStockWarning ->
                setSellerProductQtyWithLimitedStockWarning(effect.position, effect.product)

            is ProductDetailsViewEffect.SetSellerProductQtyWithOutOfStockWarning ->
                setSellerProductQtyWithOutOfStockWarning(effect.position, effect.product)

            is ProductDetailsViewEffect.AddedSellerProductToCart -> addedSellerProductToCart(effect.position)

        }
    }


    private fun backPressed() {
        super.onBackPressed()
    }

    private fun expandDescription() {
        binding.tvProductDesciption.setLines(descriptionLineCount)
        binding.tvReadMore.text = getString(R.string.read_less)
    }

    private fun collapseDescription() {
        binding.tvProductDesciption.setLines(3)
        binding.tvReadMore.text = getString(R.string.read_more)
    }

    private fun navigateToSellerProfile(sellerId: Int?) {
        Intent(this, SellerProfileActivityV1::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_SELLER_ID, sellerId)
            startActivity(this)
        }
    }

    private fun navigateToChatActivity(
        sellerId: Int?,
        sellerName: String?,
        sellerProfileImage: String?
    ) {
        Intent(this, ChatActivity::class.java).apply {
            putExtra(BundleConstant.BUNDLE_KEY_SELLER_ID, sellerId.toString())
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_TITLE, sellerName)
            putExtra(BundleConstant.BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL, sellerProfileImage)
            startActivity(this)
        }
    }

    private fun shareProduct() {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, viewModel.productData!!.absoluteUrl)
            type = "text/plain"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) startActivity(
                Intent.createChooser(
                    this,
                    "Choose an Action:",
                    null
                )
            )
            else startActivity(this)
        }
    }

    private fun navigateToBagActivity() {
        startActivity(Intent(this, NewCartActivity::class.java))
    }

    private fun navigateToDrawerActivity() {
        startActivity(Intent(this, NewDrawerActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    private fun showCustomToast(message: String?) {
        CustomToast.makeText(this, message, Toast.LENGTH_SHORT, R.style.GenericStyleableToast)
            .show()
    }

    private fun addedItemToWishList(message: String?) {
        dismissProgressDialog()
        showCustomToast(message)

        binding.btnWishlist.setImageDrawable(
            ResourcesCompat.getDrawable(
                this.resources,
                R.drawable.ic_vector_wishlist_red_24dp,
                null
            )
        )
    }

    private fun removedItemToWishList(message: String?) {
        dismissProgressDialog()
        showCustomToast(message)

        binding.btnWishlist.setImageDrawable(
            ResourcesCompat.getDrawable(
                this.resources,
                R.drawable.ic_vector_wishlist_grey_24dp,
                null
            )
        )
    }

    private fun showGuestUserLoginDialog() {
        dismissProgressDialog()
        AlertDialog.Builder(this, R.style.AlertDialogTheme).setTitle(R.string.guest_user)
            .setMessage(R.string.error_please_login_to_continue)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                Intent(this, SignInSignUpActivity::class.java).apply {
                    putExtra(BundleConstant.BUNDLE_KEY_REQ_CODE, ProductActivity.RC_ADD_TO_CART)
                    putExtra(
                        BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                        ProductActivityV2::class.java.simpleName
                    )
                    startActivityForResult(this, ProductActivity.RC_ADD_TO_CART)
                }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun addedItemToCart() {
        dismissProgressDialog()
        showSuccessFullDialog(getString(R.string.product_successfully_added))
    }

    private fun showSuccessFullDialog(msg: String) {
        binding.apply {
            ivWarning.setImageResource(R.drawable.ic_white_tick)
            tvWarningMsg.text = msg
            clDailogBox.visibility = View.VISIBLE
            clDailogBox.postDelayed(
                { binding.clDailogBox.visibility = View.GONE },
                SET_QTY_ERROR_DIALOG_TIME
            )
        }

        getBagItemsCount()
    }

    private fun setQuantity(product: ProductEntity) {
        viewModel.productData = product
        binding.apply {
            dismissProgressDialog()
            etQuantity.setText(((viewModel.productData?.getCurrentQuantity() ?: 0).toString()))
        }
    }

    private fun setQuantityWithWarning(msg: String, product: ProductEntity) {
        viewModel.productData = product
        showQuantityWarning(msg)
    }

    private fun showQuantityWarning(msg: String) {
        binding.apply {
            dismissProgressDialog()

            tvWarningMsg.text = msg
            ivWarning.setImageResource(R.drawable.ic_new_warning_icon)
            clDailogBox.visibility = View.VISIBLE
            clDailogBox.postDelayed({ binding.clDailogBox.makeGone() }, SET_QTY_ERROR_DIALOG_TIME)

            binding.etQuantity.setText(
                ((viewModel.productData?.getCurrentQuantity() ?: 0).toString())
            )
        }
    }

    private fun setProductDetails(productDetails: MutableMap<String, List<String>>) {
        val mobikulCategoryDetails = viewModel.productData?.mobikulCategoryDetails
        val categoryId = mobikulCategoryDetails?.categoryId.toString()
        val productDetailsAdapter =
            ProductDetailsAdapterV1(productDetails, categoryId, this@ProductActivityV2)
        binding.rvProductDetails.apply {
            layoutManager = LinearLayoutManager(this@ProductActivityV2)
            setHasFixedSize(true)
            adapter = productDetailsAdapter
        }
        isWishListAllowed()
        getSellerProducts()
    }

    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
    }

    private fun showChatButton() {
        binding.btnChat.makeVisible()
        initializeOnclickListeners()
    }

    private fun hideChatButton() {
        binding.btnChat.makeInvisible()
        initializeOnclickListeners()
    }

    private fun openSearchView() {
        binding.apply {
            etMaterialSearchView.makeVisible()
            etMaterialSearchView.openSearch()
        }
    }

    private fun fetchedProductDetails(productData: ProductEntity) {
        viewModel.productData = productData
        localeManager.setLocale(false, this)
        binding.apply {

            if (productData.name.isNullOrBlank()) {
                tvProductName.text =
                    intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_NAME)
                viewModel.currentProductId =
                    intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_ID).toString()
            } else {
                tvProductName.text = productData.name
                viewModel.currentProductId = productData.productId
            }

            vpProductSlider.adapter =
                ProductImageAdapter(this@ProductActivityV2, productData.images)
            tlProductSlider.setupWithViewPager(vpProductSlider, true)

            tvReducePrice.text =
                if (!productData.priceReduce.isNullOrBlank()) productData.priceReduce else productData.priceUnit

            tvUnitPrice.apply {
                text = if (!productData.priceReduce.isNullOrBlank()) productData.priceUnit else null
                background =
                    if (!productData.priceReduce.isNullOrBlank()) getDrawable(R.drawable.bg_strikethrough) else null
            }

            tvDescription.visibility =
                if (productData.description.isNullOrBlank()) View.GONE else View.VISIBLE

            divHorizontal2.visibility = tvDescription.visibility

            tvProductDesciption.apply {
                visibility =
                    if (productData.description.isNullOrBlank()) View.GONE else View.VISIBLE
                text = productData.description
                descriptionLineCount = tvProductDesciption.lineCount
                maxLines = 3
            }

            tvReadMore.visibility =
                if (tvProductDesciption.isVisibile() && descriptionLineCount > 3) View.VISIBLE else View.GONE

            divHorizontal2.visibility =
                if (productData.description.isNullOrBlank()) View.GONE else View.VISIBLE

            tvProductOutOfStock.visibility =
                if (productData.isOutOfStock()) View.VISIBLE else View.GONE

            tvPreOrder.visibility = if (productData.isPreOrder()) View.VISIBLE else View.GONE

            tvDiscountValue.text = productData.calculateDiscount()
            clDiscount.visibility =
                if (productData.priceReduce.isNullOrBlank()) View.GONE else View.VISIBLE

            tvAdditionalInfo.visibility =
                if (productData.mobikulCategoryDetails?.additionalInformation.isNullOrBlank()) View.GONE else View.VISIBLE

            divHorizontal3.visibility = tvAdditionalInfo.visibility

            tvEstimatedArrival.text = String.format(
                Locale.getDefault(),
                getString(R.string.estimated_arrival_x),
                productData.getDeliveryLeadTime()
            )


            tvAdditionalText.apply {
                visibility =
                    if (productData.mobikulCategoryDetails?.additionalInformation.isNullOrBlank()) View.GONE else View.VISIBLE
                text = productData.mobikulCategoryDetails?.additionalInformation
            }


            ivSellerInfo.apply {
                ImageHelper.load(
                    binding.ivSellerInfo,
                    productData.sellerInfo?.profileImage ?: "",
                    getDrawable(R.drawable.ic_men_avatar),
                    DiskCacheStrategy.NONE,
                    true,
                    ImageHelper.ImageType.PROFILE_PIC_SMALL
                )
            }

            tvSellerName.text =
                if (productData.sellerInfo != null) productData.sellerInfo.name else null

            tvStock.apply {
                visibility =
                    if (productData.isNever() || productData.isPreOrder() || productData.isCustom() || productData.isService()) View.GONE else View.VISIBLE
                text = if (productData.isInStock()) {
                    if (productData.isThreshold()) {
                        if (productData.availableQuantity >= productData.availableThreshold) {
                            getString(R.string.in_stock)
                        } else {
                            String.format(
                                getString(R.string.in_stock_quantity_x),
                                productData.availableQuantity
                            )
                        }
                    } else {
                        String.format(
                            getString(R.string.in_stock_quantity_x),
                            productData.availableQuantity
                        )
                    }
                } else {
                    getString(R.string.in_stock_quantity_0)
                }
            }

            btnAddToCart.apply {
                background = ResourcesCompat.getDrawable(
                    resources,
                    if (productData.isOutOfStock()) R.drawable.btn_add_to_cart_disabled
                    else R.drawable.btn_add_to_cart_enabled,
                    null
                )
                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        if (productData.isOutOfStock()) R.color.white else R.color.background_orange,
                        null
                    )
                )

            }


            btnWishlist.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    if (productData.addedToWishlist ?: false) R.drawable.ic_vector_wishlist_red_24dp
                    else R.drawable.ic_vector_wishlist_grey_24dp,
                    null
                )
            )


            nsvProductDetails.isSmoothScrollingEnabled = true
            nsvProductDetails.makeVisible()

        }
        convertProductDetails(productData)
    }

    private fun setBackButtonInToolbar() {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        checkOnNewIntent()
        checkChatFeatureEnabled()
    }

    private fun showBagItemCount(count: String) {
        binding.apply {
            tvBadge.makeVisible()
            tvBadge.text = count
        }
    }

    private fun hideBagItemCount() {
        binding.tvBadge.makeInvisible()
    }

    private fun handleFirstTimeLaunch(newIntent: Intent?) {
        intent = newIntent
    }

    private fun closeSearchView() {
        binding.etMaterialSearchView.closeSearch()
    }

    private fun reloadProductActivity(newIntent: Intent?) {
        startActivity(newIntent)
        finish()
    }

    private fun setOnClickListeners() {

        binding.tbProductDetails.setNavigationOnClickListener {
            triggerIntent(ProductDetailsIntent.BackPressed)
        }

        binding.tvReadMore.setOnClickListener {
            triggerIntent(ProductDetailsIntent.ReadMoreDescription)
        }

        binding.ivCart.setOnClickListener {
            triggerIntent(ProductDetailsIntent.NavigateToBagActivity)
        }

        binding.ivDrawer.setOnClickListener {
            triggerIntent(ProductDetailsIntent.NavigateToDrawerActivity)
        }

        binding.tvSearch.setOnClickListener {
            triggerIntent(ProductDetailsIntent.OpenSearchView)
        }

        binding.tvSellerName.setOnClickListener {
            triggerIntent(ProductDetailsIntent.NavigateToSellerProfile(viewModel.productData?.sellerInfo?.id))
        }

        binding.btnMinus.setOnClickListener {
            val qty = viewModel.productData?.getCurrentQuantity()?.minus(1)
            triggerIntent(ProductDetailsIntent.HandleProductQuantity(qty, viewModel.productData))
        }

        binding.btnPlus.setOnClickListener {
            val qty = viewModel.productData?.getCurrentQuantity()?.plus(1)
            triggerIntent(ProductDetailsIntent.HandleProductQuantity(qty, viewModel.productData))
        }


        binding.btnWishlist.setOnClickListener {
            triggerIntent(ProductDetailsIntent.WishlistBtnClicked)
        }

        binding.btnAddToCart.setOnClickListener {
            triggerIntent(
                ProductDetailsIntent.AddToCart(
                    viewModel.productData,
                    viewModel.productData?.getCurrentQuantity()
                )
            )
        }


        binding.btnShare.setOnClickListener {
            triggerIntent(ProductDetailsIntent.ShareProduct)
        }

        binding.btnChat.setOnClickListener {
            val seller = viewModel.productData?.sellerInfo
            triggerIntent(
                ProductDetailsIntent.NavigateToChatActivity(
                    seller?.id,
                    seller?.name,
                    seller?.profileImage
                )
            )
        }


        binding.etQuantity.apply {

            doOnTextChanged { text, _, _, _ ->
                val qty = if (!text.isNullOrBlank()) text.toString().toInt() else null
                if (viewModel.productData?.getCurrentQuantity() != qty && qty != null)
                    triggerIntent(
                        ProductDetailsIntent.HandleProductQuantity(
                            qty,
                            viewModel.productData
                        )
                    )
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (text.isNullOrBlank())
                        triggerIntent(
                            ProductDetailsIntent.HandleProductQuantity(
                                1, viewModel.productData
                            )
                        )
                    else {
                        val qty = text.toString().toInt()
                        triggerIntent(
                            ProductDetailsIntent.HandleProductQuantity(
                                qty,
                                viewModel.productData
                            )
                        )
                    }
                    return@setOnEditorActionListener true
                }
                false
            }

        }
        initializeStatusBarColor()
    }

    override fun triggerIntent(intent: ProductDetailsIntent) {
        lifecycleScope.launch {
            viewModel.intents.send(intent)
        }
    }

    private fun getBagItemsCount() = triggerIntent(ProductDetailsIntent.GetBagItemsCount)

    private fun checkOnNewIntent() = triggerIntent(ProductDetailsIntent.CheckOnNewIntent)

    private fun checkIntentExtras(intent: Intent?) =
        triggerIntent(ProductDetailsIntent.CheckIntentExtras(intent))

    private fun isWishListAllowed() = triggerIntent(ProductDetailsIntent.IsWishListAllowed)

    private fun getSellerProducts() = triggerIntent(ProductDetailsIntent.GetSellerProducts)

    private fun checkChatFeatureEnabled() =
        triggerIntent(ProductDetailsIntent.CheckChatFeatureEnabled)

    private fun initializeOnclickListeners() =
        triggerIntent(ProductDetailsIntent.InitializeOnclickListeners)

    private fun initializeStatusBarColor() =
        triggerIntent(ProductDetailsIntent.InitializeStatusBarColor)

    private fun convertProductDetails(productData: ProductEntity) =
        triggerIntent(ProductDetailsIntent.ConvertProductDetails(productData))

    private fun getProductData() =
        triggerIntent(ProductDetailsIntent.GetProductDetails(intent.extras?.getInt(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID)))

    // below are for seller products

    private fun addedSellerProductToCart(position: Int) {
        dismissProgressDialog()
        showSuccessFullDialog(getString(R.string.product_successfully_added))
    }

    private fun setSellerProducts(productListEntity: ProductListEntity) {
        dismissProgressDialog()
        if (productListEntity.products.isEmpty().not()) {
            binding.tvProductSellersText.makeVisible()

            viewModel.sellerProducts = productListEntity.products

            productSellersAdapter =
                ProductSellersAdapter(viewModel.sellerProducts, this@ProductActivityV2)
            binding.rvSellerProducts.apply {
                layoutManager = LinearLayoutManager(this@ProductActivityV2)
                setHasFixedSize(true)
                adapter = productSellersAdapter
            }


            /*

            // Todo("Need to be used in Price Comparison Milestone 1B")


            binding.apply {
                val currentProductId = intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_ID)
                val currentTemplateId = intent.extras?.getInt(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID)


                if (viewModel.productData?.productId != currentProductId && viewModel.productData?.templateId != currentTemplateId && isFirstTimeLaunch) {
                    isFirstTimeLaunch = false
                    nsvProductDetails.smoothScrollBy(0, rvChildProducts.y.toInt(), 3000)

                    val productIndex = productListEntity.products.indexOfFirst { it.productId.toString() == currentProductId }

                    rvChildProducts.post {
                        rvChildProducts.smoothScrollToPosition(productIndex)
                    }

                    rvChildProducts.post {
                        childProductsAdapter.apply {
                            highlight = true
                            notifyItemChanged(productIndex)
                        }
                    }

                    rvChildProducts.postDelayed({ childProductsAdapter.notifyItemChanged(0) }, 3250)

                }

            }

            */
        }
    }

    private fun setSellerProductQtyWithOutOfStockWarning(
        position: Int,
        sellerProduct: ProductEntity
    ) {
        viewModel.sellerProducts[position] = sellerProduct
        showQuantityWarning(getString(R.string.product_out_of_stock))
        binding.rvSellerProducts.post {
            productSellersAdapter.apply {
                updateProduct(sellerProduct, position)
                notifyItemChanged(position)
            }
        }
    }

    private fun setSellerProductQtyWithLimitedStockWarning(
        position: Int,
        sellerProduct: ProductEntity
    ) {
        viewModel.sellerProducts[position] = sellerProduct
        showQuantityWarning(getString(R.string.product_quantity_exceeding))
        binding.rvSellerProducts.post {
            productSellersAdapter.apply {
                updateProduct(sellerProduct, position)
                notifyItemChanged(position)
            }
        }
    }

    private fun setSellerProductQuantity(position: Int, sellerProduct: ProductEntity) {
        viewModel.sellerProducts[position] = sellerProduct
        binding.rvSellerProducts.post {
            productSellersAdapter.apply {
                updateProduct(sellerProduct, position)
                notifyItemChanged(position)
            }
        }
    }

    override fun onSellerProductPlusClick(position: Int) {
        val data = viewModel.sellerProducts[position]
        val qty = (data.getCurrentQuantity()) + 1
        triggerIntent(ProductDetailsIntent.HandleProductQtyForSellerProduct(qty, data, position))
    }

    override fun onSellerProductMinusClick(position: Int) {
        val data = viewModel.sellerProducts[position]
        val qty = (data.getCurrentQuantity()) - 1
        triggerIntent(ProductDetailsIntent.HandleProductQtyForSellerProduct(qty, data, position))

    }

    override fun onSellerProductEditTextClick(qty: Int, position: Int) {
        val data = viewModel.sellerProducts[position]
        triggerIntent(ProductDetailsIntent.HandleProductQtyForSellerProduct(qty, data, position))
    }

    override fun onSellerProductAddToCartClick(position: Int) {
        val data = viewModel.sellerProducts[position]

        triggerIntent(
            ProductDetailsIntent.AddSellerProductToCart(
                data,
                position
            )
        )
    }

    override fun onSellerProductSellerNameClick(position: Int) {
        val sellerId = viewModel.sellerProducts[position].sellerInfo?.id
        triggerIntent(ProductDetailsIntent.NavigateToSellerProfile(sellerId))
    }

    override fun onSellerProductChatClick(position: Int) {
        val seller = viewModel.sellerProducts[position].sellerInfo
        triggerIntent(
            ProductDetailsIntent.NavigateToChatActivity(
                seller?.id,
                seller?.name,
                seller?.profileImage
            )
        )
    }

}