package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.product.ProductDetailsAdapterV1
import com.webkul.mobikul.odoo.adapter.product.ProductImageAdapter
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.utils.HTTP_ERROR_BAD_REQUEST
import com.webkul.mobikul.odoo.core.utils.HTTP_RESOURCE_CREATED
import com.webkul.mobikul.odoo.core.utils.HTTP_RESOURCE_NOT_FOUND
import com.webkul.mobikul.odoo.core.utils.HTTP_RESPONSE_OK
import com.webkul.mobikul.odoo.data.request.CartProductItemRequest
import com.webkul.mobikul.odoo.data.request.CartProductsRequest
import com.webkul.mobikul.odoo.data.response.models.CartProductsResponse
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.data.response.models.GetCartId
import com.webkul.mobikul.odoo.database.SaveData
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.databinding.ActivityProductV1Binding
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.handler.product.ProductActivityHandler
import com.webkul.mobikul.odoo.helper.*
import com.webkul.mobikul.odoo.model.generic.ProductData
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.ui.cart.NewCartActivity
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProductActivityV1 : BaseActivity() {
    private val QTY_ZERO = 0
    private val MAX_QUANTITY_COUNT = 9999999
    private val TAG = "ProductActivityV1"
    private val RC_ADD_TO_CART = 1001
    private val RC_BUY_NOW = 1002
    lateinit var binding: ActivityProductV1Binding
    private var currentProductId: String = ""
    private var expandable = false
    private val DESCRIPTION_TEXTVIEW_LIMIT = 4
    var lineCount = 0
    private lateinit var sweetAlertDialog : SweetAlertDialog

    private lateinit var productData: ProductData

    private var categoryId : String = ""
    private val productDataCustomObserver: CustomObserver<ProductData?> =
            object : CustomObserver<ProductData?>(this) {
                override fun onNext(productData: ProductData) {
                    super.onNext(productData)
                    if (productData.isAccessDenied) {
                        sweetAlertDialog = AlertDialogHelper.getAlertDialog(
                                this@ProductActivityV1,SweetAlertDialog.WARNING_TYPE,
                                getString(R.string.error_login_failure),
                                getString(R.string.access_denied_message), false, false)
                        sweetAlertDialog.setConfirmClickListener {
                            sweetAlertDialog.dismiss()
                            AppSharedPref.clearCustomerData(this@ProductActivityV1)
                            startActivity(
                                    Intent(this@ProductActivityV1, SignInSignUpActivity::class.java)
                            .
                                putExtra(
                                        BUNDLE_KEY_CALLING_ACTIVITY,
                                        ProductActivityV1::class.java.simpleName
                                ))
                        }
                    } else {
                        setDataAfterFetchData(productData)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                sweetAlertDialog = AlertDialogHelper.getAlertDialog(
                    this@ProductActivityV1, SweetAlertDialog.WARNING_TYPE,
                    getString(R.string.error_something_went_wrong),
                    getString(R.string.try_again_later_text), false, false)

                sweetAlertDialog.setConfirmClickListener {
                    sweetAlertDialog.dismiss()
                    finish()
                }
            }

                override fun onComplete() {
                }
            }

    private var returnedWithResult = false

    private fun setDataAfterFetchData(productData: ProductData) {
        this.productData = productData
        binding.apply {
            data = productData
            if (TextUtils.isEmpty(productData.name)) {
                title = intent.extras?.getString(BUNDLE_KEY_PRODUCT_NAME)
                currentProductId = intent.extras?.getString(BUNDLE_KEY_PRODUCT_ID).toString()
            } else {
                title = productData.name
                currentProductId = productData.productId
            }
        }
        SaveData(this@ProductActivityV1, productData)
        binding.apply {
            handler = ProductActivityHandler(this@ProductActivityV1, productData)
            executePendingBindings()
            vpProductSlider.adapter =
                    ProductImageAdapter(this@ProductActivityV1, productData.images)
            tlProductSlider.setupWithViewPager(vpProductSlider, true)
        }
        quantityEditTextOnChange()
        setProductDetails(productData)
        getDescriptionLineCount()
    }

    private fun quantityEditTextOnChange() {
        binding.tvQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(qty: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(qty: Editable?) {}

            override fun onTextChanged(quantity: CharSequence?, start: Int, before: Int, count: Int) {
                if (quantity != null) {
                    if (quantity.isNotEmpty()) {
                        val qty = quantity.toString().toInt()
                        onQuantityChanged(qty)
                    }
                }
            }
        })

        binding.tvQuantity.setOnEditorActionListener { _, actionId, _ ->
            checkQuantityActionDone(actionId)
            false
        }
    }

    private fun onQuantityChanged(qty: Int) {
        val finalQuantity = isQuantityExceeding(qty)
        productData.quantity = finalQuantity
    }


    private fun getDescriptionLineCount() {
        if (binding.tvProductDesciption.visibility != View.GONE) {
            binding.tvProductDesciption.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.tvProductDesciption.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    lineCount = binding.tvProductDesciption.layout.lineCount
                    if (lineCount > DESCRIPTION_TEXTVIEW_LIMIT) {
                        expandDescription()
                    }
                }
            })
        }
    }

    private fun expandDescription() {
        binding.apply {
            tvProductDesciption.maxLines = DESCRIPTION_TEXTVIEW_LIMIT
            tvReadMore.visibility = View.VISIBLE
        }
        expandable = true
    }

    private fun setProductDetails(productData: ProductData) {
        val productDetails = getProductDetails(productData)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvProductDetails.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = ProductDetailsAdapterV1(productDetails, categoryId, this@ProductActivityV1)
        }
    }

    private fun getProductDetails(productData: ProductData): MutableMap<String, List<String>> {
        val details = mutableMapOf<String, List<String>>()
        val mobikulCategoryDetails = productData.mobikulCategoryDetails
        details[getString(R.string.product_details_category)] = mutableListOf(mobikulCategoryDetails.category.toString())
        categoryId = mobikulCategoryDetails.categoryId.toString()
        details[getString(R.string.product_details_brand)] =
                mutableListOf(productData.getBrandName())
        details[getString(R.string.product_details_active_ingredients)] =
                mutableListOf(mobikulCategoryDetails.activeIngredients.toString())
        details[getString(R.string.product_details_dosage)] =
                mutableListOf(mobikulCategoryDetails.dosage.toString())
        val crops: MutableList<String> = mutableListOf<String>()
        for (crop in mobikulCategoryDetails.crops) {
            crops.add(crop)
        }
        details[getString(R.string.product_details_crops)] = crops
        if (mobikulCategoryDetails.isOrganic)
            details[getString(R.string.product_details_organic)] =
                    mutableListOf(getString(R.string.yes))
        else
            details[getString(R.string.product_details_organic)] =
                    mutableListOf(getString(R.string.no))
        details[getString(R.string.product_details_weight)] =
                mutableListOf(mobikulCategoryDetails.weight.toString())
        details[getString(R.string.product_details_mrp)] =
                mutableListOf(mobikulCategoryDetails.mrp.toString())
        details[getString(R.string.product_details_planting_method)] =
                mutableListOf(mobikulCategoryDetails.plantingMethod.toString())
        details[getString(R.string.product_details_duration_of_effect)] =
                mutableListOf(mobikulCategoryDetails.durationOfEffect.toString())
        details[getString(R.string.product_details_planting_spacing)] =
                mutableListOf(mobikulCategoryDetails.plantSpacing.toString())
        details[getString(R.string.product_details_pests_and_diseases)] =
                mutableListOf(mobikulCategoryDetails.pestsAndDiseases.toString())
        details[getString(R.string.product_details_application_method)] =
                mutableListOf(mobikulCategoryDetails.applicationMethod.toString())
        details[getString(R.string.product_details_frequency_of_application)] =
                mutableListOf(mobikulCategoryDetails.frequencyOfApplication.toString())

        val detailsUpdated = mutableMapOf<String, List<String>>()
        for (item in details.entries.iterator()) {
            val checkEmptyList =
                    ((item.value.toString() == "[${getString(R.string.product_details_weight_zero)}]") or (item.value.toString() == "[]"))
            if (!checkEmptyList) {
                detailsUpdated[item.key] = item.value
            }
        }
        return detailsUpdated
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_v1)
        initDialog()
        FirebaseAnalyticsImpl.logViewItem(
                this,
                intent.extras?.getString(BUNDLE_KEY_PRODUCT_ID),
                intent.extras?.getString(BUNDLE_KEY_PRODUCT_NAME)
        )
        showBackButton(true)
        onNewIntent(intent)
        setChatButton()
        setAllListeners()
        launchNewDrawerActivity()
        getBagItemsCount()
        setStatusBarColor()
    }

    override fun onResume() {
        super.onResume()
        getBagItemsCount()
        Helper.hideKeyboard(this)
        onResumeHandler()
    }

    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
        }
    }

    private fun initDialog() {
        sweetAlertDialog = AlertDialogHelper.getAlertDialog(this,
            SweetAlertDialog.PROGRESS_TYPE, getString(R.string.please_wait),"", false,false);
    }



    private fun onResumeHandler() {
        if (!returnedWithResult) {
            val sqlLiteDbHelper = SqlLiteDbHelper(this)
            val productData = sqlLiteDbHelper.getProductScreenData(
                    intent.extras?.getString(BUNDLE_KEY_PRODUCT_ID)
            )
            if (!NetworkHelper.isNetworkAvailable(this) && productData != null) {
                setDataAfterFetchData(productData)
            } else {
                ApiConnection.getProductData(
                        this,
                        intent.extras?.getString(BUNDLE_KEY_PRODUCT_ID),
                        intent.extras?.getString(BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID)
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(productDataCustomObserver)
            }
        } else {
            returnedWithResult = false
        }
    }

    private fun setChatButton() {
        if (FirebaseRemoteConfigHelper.isChatFeatureEnabled && !AppSharedPref.isSeller(this)) {
            binding.btnChat.visibility = View.VISIBLE
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (currentProductId == "") {
            setIntent(intent)
            binding.data = null
        } else if (currentProductId == intent?.extras?.getString(BUNDLE_KEY_PRODUCT_ID)
                        .toString()
        ) {
            binding.etMaterialSearchView.closeSearch()
        } else {
            startActivity(intent)
            finish()
            binding.data = null
        }
    }

    override fun getScreenTitle(): String {
        return TAG
    }

    fun getBagItemsCount() {
        val count = AppSharedPref.getNewCartCount(this)
        binding.apply {
            if (count != ApplicationConstant.MIN_ITEM_TO_BE_SHOWN_IN_CART) {
                tvBadge.makeVisible()
                if (count < ApplicationConstant.MAX_ITEM_TO_BE_SHOWN_IN_CART)
                    tvBadge.text = count.toString()
                else
                    tvBadge.text = getString(R.string.text_nine_plus)
            } else {
                tvBadge.makeGone()
            }
        }
    }

    override fun onBackPressed() {
        isSearchViewOpen()
        if (supportFragmentManager.backStackEntryCount == 1) {
            onResume()
        }
        super.onBackPressed()
    }

    private fun isSearchViewOpen() {
        if (binding.etMaterialSearchView.isOpen) {
            binding.etMaterialSearchView.closeSearch()
            try {
                val inputManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                        binding.tvSearch.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
    }

    fun showWarning(emptyStock: Boolean) {
        binding.apply {
            if (emptyStock) {
                tvWarningMsg.text = getString(R.string.product_out_of_stock)
            } else {
                tvWarningMsg.text = getString(R.string.product_quantity_exceeding)
            }
            ivWarning.setImageResource(R.drawable.ic_new_warning_icon)
            clDailogBox.visibility = View.VISIBLE
            clDailogBox.postDelayed(
                    Runnable { binding.clDailogBox.setVisibility(View.GONE) },
                    1500
            )
        }
    }

    fun showSuccessfullDialog() {
        binding.apply {
            ivWarning.setImageResource(R.drawable.ic_white_tick)
            tvWarningMsg.text = getString(R.string.product_successfully_added)
            clDailogBox.visibility = View.VISIBLE
            clDailogBox.postDelayed(
                    Runnable { binding.clDailogBox.setVisibility(View.GONE) },
                    1500
            )
        }
    }


    private fun checkQuantityActionDone(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.tvQuantity.text.isNullOrEmpty()) {
                onQuantityChanged(1)
            } else {
                val qty = binding.tvQuantity.text.toString().toInt()
                onQuantityChanged(qty)
            }
        }
    }

    private fun setAllListeners() {
        binding.apply {

            tbProductDetails.setNavigationOnClickListener {
                onBackPressed()
            }

            ivCart.setOnClickListener {
                navigateToBagScreen()
            }

            tvSearch.setOnClickListener {
                etMaterialSearchView.visibility = View.VISIBLE
                etMaterialSearchView.openSearch()
            }

            btnAddToCart.setOnClickListener {
                addProductToCart()
            }

            tvReadMore.setOnClickListener {
                if (expandable) {
                    tvProductDesciption.maxLines = lineCount
                    tvReadMore.text = getString(R.string.read_less)
                } else {
                    tvProductDesciption.maxLines = DESCRIPTION_TEXTVIEW_LIMIT
                    tvReadMore.text = getString(R.string.read_more)
                }
                expandable = !expandable
            }

            btnMinus.setOnClickListener {
                onClickMinus(productData.quantity)
            }

            btnPlus.setOnClickListener {
                onClickPlus(productData.quantity)
            }
        }
    }

    private fun addProductToCart() {
        val cartId = AppSharedPref.getCartId(this);
        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE)
            getCartIdToAddProduct()
        else {
            addToCart(false)
        }
    }

    private fun navigateToBagScreen() {
        val cartId = AppSharedPref.getCartId(this);
        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE)
            getCartId()
        else {
            startActivity(Intent(this, NewCartActivity::class.java))
        }
    }

    //TODO: need to optimize later
    private fun createCartToAddProduct(customerId: Int) {
        ApiConnection.createCart(this, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){

                    override fun onNext(response: CartBaseResponse<GetCartId>) {
                        super.onNext(response)
                        AppSharedPref.setCartId(this@ProductActivityV1, response.result.cartId)
                        sweetAlertDialog.dismiss()
                        addToCart(false)    //adding item to cart
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        sweetAlertDialog.dismiss() //can't add item to cart
                    }

                    override fun onComplete() {
                        super.onComplete()
                        sweetAlertDialog.dismiss()
                    }
                })
    }

    private fun createCart(customerId: Int) {
        ApiConnection.createCart(this, customerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){

                override fun onNext(response: CartBaseResponse<GetCartId>) {
                    super.onNext(response)
                    AppSharedPref.setCartId(this@ProductActivityV1, response.result.cartId)
                    sweetAlertDialog.dismiss()
                    addToCart(false)
                    startActivity(Intent(this@ProductActivityV1, NewCartActivity::class.java))
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    sweetAlertDialog.dismiss()
                }

                override fun onComplete() {
                    super.onComplete()
                    sweetAlertDialog.dismiss()
                }
            })
    }

    private fun addToCart(isBuyNow: Boolean) {
        if (!AppSharedPref.isLoggedIn(this)) {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            builder.setTitle(R.string.guest_user)
                .setMessage(R.string.error_please_login_to_continue)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    val intent = Intent(this, SignInSignUpActivity::class.java)
                    intent.putExtra(BUNDLE_KEY_REQ_CODE, if (isBuyNow) ProductActivity.RC_BUY_NOW else ProductActivity.RC_ADD_TO_CART)
                    intent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ProductActivityV1::class.java.simpleName)
                    startActivityForResult(intent, if (isBuyNow) ProductActivity.RC_BUY_NOW else ProductActivity.RC_ADD_TO_CART)
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
            return
        }
        var productId = ""
        if (productData.variants.isEmpty()) {
            productId = productData.productId
        }
        if (checkQuantity(productData.quantity)) {
            showWarning(true)
            return
        }
        if (productId.isEmpty()) {
            SnackbarHelper.getSnackbar(this, getString(R.string.error_could_not_add_to_bag_pls_try_again),
                Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
            return
        }
        if (!productData.isNever && productData.quantity > productData.availableQuantity) {
            SnackbarHelper.getSnackbar(this, getString(R.string.product_not_available_in_this_quantity),
                Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
            return
        }
        val cartId = AppSharedPref.getCartId(this)
        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE) {
            SnackbarHelper.getSnackbar(this, getString(R.string.error_could_not_add_to_bag_pls_try_again),
                Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
            return
        }

        val req = CartProductItemRequest(productData.productId.toInt(), binding.tvQuantity.text.toString().toInt(), 0)
        ApiConnection.addProductToCartV1(this, cartId, CartProductsRequest(arrayListOf(req)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<CartProductsResponse>>(this){
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    AlertDialogHelper.showDefaultProgressDialog(this@ProductActivityV1)
                }

                override fun onNext(response: CartBaseResponse<CartProductsResponse>) {
                    super.onNext(response)
                    if (response.statusCode >= HTTP_ERROR_BAD_REQUEST) {
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(this@ProductActivityV1, getString(R.string.error_login_failure), getString(R.string.access_denied_message)) { sweetAlertDialog ->
                            sweetAlertDialog.dismiss()
                            AppSharedPref.clearCustomerData(this@ProductActivityV1)

                            Intent(this@ProductActivityV1, SignInSignUpActivity::class.java).apply {
                                putExtra(BUNDLE_KEY_CALLING_ACTIVITY, this@ProductActivityV1.javaClass.simpleName)
                                startActivity(this)
                            }
                        }
                    } else {
                        AnalyticsImpl.trackAddItemToBagSuccessful(productData.quantity, productData.productId, productData.seller.marketplaceSellerId, productData.name)
                        //TODO optimise this
                        AppSharedPref.setNewCartCount(this@ProductActivityV1, response.result.cartCount)
                        getBagItemsCount()
                        if (isBuyNow) {
                            if (response.statusCode == HTTP_RESPONSE_OK ||
                                response.statusCode == HTTP_RESOURCE_CREATED) {
                                IntentHelper.beginCheckout(this@ProductActivityV1)
                            } else {
                                showQuantityWarning(response.message)
                            }
                        } else {
                            if (response.statusCode == HTTP_RESPONSE_OK ||
                                response.statusCode == HTTP_RESOURCE_CREATED) {
                                    showSuccessfullDialog()
                            } else {
                                showWarning(false)
                            }
                        }
                    }
                }

                override fun onComplete() {}
            })

    }


    private fun checkQuantity(qty: Int): Boolean {
        return qty == QTY_ZERO
    }

    private fun showQuantityWarning(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomImage(R.drawable.ic_warning)
            .setTitleText("")
            .setContentText(message)
            .setConfirmText(getString(R.string.continue_))
            .setConfirmClickListener { sweetAlertDialog: SweetAlertDialog -> sweetAlertDialog.dismiss() }
            .show()
    }

    private fun onClickMinus(qty: Int) {
        var qty = qty
        if (qty <= QTY_ZERO) {
            productData.quantity = QTY_ZERO
        } else {
            qty--
            productData.quantity = qty
        }
    }

    private fun onClickPlus(qty: Int) {
        var qty = qty
        qty++
        if (qty > MAX_QUANTITY_COUNT) {
            qty--
        }
        qty = isQuantityExceeding(qty)
        productData.quantity = qty
    }

    private fun isQuantityExceeding(qty: Int): Int {
        if (productData.isOutOfStock) {
            showWarning(true)
            return productData.availableQuantity
        } else if (!productData.isNever && qty > productData.availableQuantity) {
            showWarning(false)
            return productData.availableQuantity
        }
        return qty
    }

    //TODO:Need to optimize later
    private fun getCartIdToAddProduct() {
        val customerId = AppSharedPref.getCustomerId(this).toInt()
        ApiConnection.checkIfCartExists(this, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        sweetAlertDialog.show()
                    }

                    override fun onNext(response: CartBaseResponse<GetCartId>) {
                        super.onNext(response)
                        if(response.statusCode == HTTP_RESOURCE_NOT_FOUND)
                            createCartToAddProduct(customerId)
                        else {
                            AppSharedPref.setCartId(this@ProductActivityV1, response.result.cartId)
                            sweetAlertDialog.dismiss()
                            addToCart(false)
                        }
                    }

                    override fun onError(t: Throwable) {
                        createCartToAddProduct(customerId)
                    }
                })
    }

    private fun getCartId() {
        val customerId = AppSharedPref.getCustomerId(this).toInt()
        ApiConnection.checkIfCartExists(this, customerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    sweetAlertDialog.show()
                }

                override fun onNext(response: CartBaseResponse<GetCartId>) {
                    super.onNext(response)
                    if(response.statusCode == HTTP_RESOURCE_NOT_FOUND)
                        createCart(customerId)
                    else {
                        AppSharedPref.setCartId(this@ProductActivityV1, response.result.cartId)
                        sweetAlertDialog.dismiss()
                        startActivity(Intent(this@ProductActivityV1, NewCartActivity::class.java))
                    }
                }

                override fun onError(t: Throwable) {
                    createCart(customerId)
                }
            })
    }



    private fun launchNewDrawerActivity() {
        binding.ivDrawer.setOnClickListener {
            Intent(this, NewDrawerActivity::class.java).apply {
                startActivity(this)
            }
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

}