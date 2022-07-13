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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.product.ProductDetailsAdapterV1
import com.webkul.mobikul.odoo.adapter.product.ProductImageAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.*
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.database.SaveData
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.databinding.ActivityProductV1Binding
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl
import com.webkul.mobikul.odoo.handler.product.ProductActivityHandler
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.Helper
import com.webkul.mobikul.odoo.helper.NetworkHelper
import com.webkul.mobikul.odoo.model.generic.ProductData
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_product_v1.*

class ProductActivityV1 : BaseActivity() {
    private val TAG = "ProductActivityV1"
    private val RC_ADD_TO_CART = 1001
    private val RC_BUY_NOW = 1002
    lateinit var binding: ActivityProductV1Binding
    private var currentProductId: String = ""
    private var expandable = false
    private val DESCRIPTION_TEXTVIEW_LIMIT = 4
    var lineCount = 0
    private var categoryId : String = ""
    private val productDataCustomObserver: CustomObserver<ProductData?> =
        object : CustomObserver<ProductData?>(this) {
            override fun onNext(productData: ProductData) {
                super.onNext(productData)
                if (productData.isAccessDenied) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(
                        this@ProductActivityV1,
                        getString(R.string.error_login_failure),
                        getString(R.string.access_denied_message)
                    ) { sweetAlertDialog ->
                        sweetAlertDialog.dismiss()
                        AppSharedPref.clearCustomerData(this@ProductActivityV1)
                        val intent =
                            Intent(this@ProductActivityV1, SignInSignUpActivity::class.java)
                        intent.apply {
                            putExtra(
                                BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY,
                                ProductActivityV1::class.java.simpleName
                            )
                            startActivity(this)
                        }
                    }
                } else {
                    setDataAfterFetchData(productData)
                }
            }

            override fun onError(t: Throwable) {
                super.onError(t)
            }

            override fun onComplete() {
            }
        }

    private var returnedWithResult = false

    private fun setDataAfterFetchData(productData: ProductData) {
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

    private fun getDescriptionLineCount(){
        if(binding.tvProductDesciption.visibility != View.GONE) {
            binding.tvProductDesciption.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.tvProductDesciption.viewTreeObserver.removeOnGlobalLayoutListener(this);
                    lineCount = binding.tvProductDesciption.layout.lineCount
                    if(lineCount > DESCRIPTION_TEXTVIEW_LIMIT){
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
            adapter = ProductDetailsAdapterV1(productDetails,categoryId,this@ProductActivityV1)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_v1)

        FirebaseAnalyticsImpl.logViewItem(
            this,
            intent.extras?.getString(BUNDLE_KEY_PRODUCT_ID),
            intent.extras?.getString(BUNDLE_KEY_PRODUCT_NAME)
        )
        showBackButton(true)
        onNewIntent(intent)
        setChatButton()
        setAllListners()
        launchNewDrawerActivity()
        getBagItemsCount()
        setStatusBarColor()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
    }

    fun getHomePageResponse(): HomePageResponse? =
        intent.getParcelableExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE)

    override fun onResume() {
        super.onResume()
        getBagItemsCount()
        Helper.hideKeyboard(this)
        onResumeHandler()
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
        val count = AppSharedPref.getCartCount(this, ApplicationConstant.MIN_ITEM_TO_BE_SHOWN_IN_CART)
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

    private fun quantityEditTextOnChange() {
        binding.tvQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(qty: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(
                quantity: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (quantity != null) {
                    if (quantity.isNotEmpty()) {
                        val qty = quantity.toString().toInt()
                        binding.handler?.onClickEditext(qty)
                    }
                }
            }

            override fun afterTextChanged(qty: Editable?) {

            }

        })

        binding.tvQuantity.setOnEditorActionListener { _, actionId, _ ->
            checkQuantityActionDone(actionId)
            false
        }
    }

    private fun checkQuantityActionDone(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.tvQuantity.text.isNullOrEmpty()) {
                binding.handler?.onClickEditext(1)
            } else {
                val qty = binding.tvQuantity.text.toString().toInt()
                binding.handler?.onClickEditext(qty)
            }
        }
    }

    private fun setAllListners() {
        binding.tbProductDetails.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.ivCart.setOnClickListener {
            startActivity(Intent(this, BagActivity::class.java))
        }

        binding.tvSearch.setOnClickListener {
            binding.etMaterialSearchView.visibility = View.VISIBLE
            binding.etMaterialSearchView.openSearch()
        }

        binding.apply {
            tvReadMore.setOnClickListener {
                if(expandable){
                    tvProductDesciption.maxLines = lineCount
                    tvReadMore.text = getString(R.string.read_less)
                }else{
                    tvProductDesciption.maxLines = DESCRIPTION_TEXTVIEW_LIMIT
                    tvReadMore.text = getString(R.string.read_more)
                }
                expandable = !expandable
            }
        }
    }

    private fun launchNewDrawerActivity() {
        binding.ivDrawer.setOnClickListener {
            val intent = Intent(this, NewDrawerActivity::class.java)
            intent.let {
                it.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, getHomePageResponse())
                startActivity(it)
            }
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

}