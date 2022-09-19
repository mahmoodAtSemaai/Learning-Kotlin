package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.adapter.loyalty.LoyaltyHistoryListAdapter
import com.webkul.mobikul.odoo.adapter.loyalty.LoyaltyTermsListAdapter
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.utils.HTTP_RESOURCE_NOT_FOUND
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.data.response.models.GetCartId
import com.webkul.mobikul.odoo.databinding.ActivityLoyaltyHistoryBinding
import com.webkul.mobikul.odoo.dialog_frag.WhatsNewDialogFragment
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.CustomerHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.ReferralResponse
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyHistoryResponse
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsData
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsResponse
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTransactionData
import com.webkul.mobikul.odoo.ui.cart.NewCartActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoyaltyHistoryActivity : BaseActivity() {

    val TAG = "LoyaltyHistoryActivity"
    private lateinit var binding: ActivityLoyaltyHistoryBinding
    var limit = 10
    var offset = 0
    val miniListSize = 4
    var isFirstCall = true
    var loyaltyHistory: MutableList<LoyaltyTransactionData> = mutableListOf<LoyaltyTransactionData>()
    var loyaltyBanners: List<LoyaltyTermsData> = listOf<LoyaltyTermsData>()
    var customerId : String = ""
    var count = 0
    private lateinit var sweetAlertDialog : SweetAlertDialog


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loyalty_history)
        customerId = AppSharedPref.getCustomerId(this@LoyaltyHistoryActivity)
        changeStatusbarColor()
        setIconOnClicks()
        getSemaaiPoints()
        getLoyaltyTerms()
        showLoyaltyPointsDialog()
        initDialog()
    }

    override fun onResume() {
        super.onResume()
        getBagItemsCount()
    }

    override fun getScreenTitle(): String {
        return TAG
    }

    private fun setIconOnClicks() {
        binding.apply {
            ivBack.setOnClickListener { onBackPressed() }
            ivWishlist.setOnClickListener { openWishlist() }
            ivCartIcon.setOnClickListener { navigateToCartScreen() }
            tvMoreHistory.setOnClickListener { showLoyaltyhistoryList(loyaltyHistory) }
            tvNext.setOnClickListener {
                offset += limit
                getLoyaltyPointsHistory(limit, offset)
            }
            tvPrevious.setOnClickListener {
                offset -= limit
                getLoyaltyPointsHistory(limit, offset)
            }
        }
    }

    private fun getBagItemsCount() {
        val count = AppSharedPref.getNewCartCount(this@LoyaltyHistoryActivity)
        binding.apply {
            if (count != ApplicationConstant.MIN_ITEM_TO_BE_SHOWN_IN_CART) {

                tvCartItemsCount.makeVisible()
                if (count < ApplicationConstant.MAX_ITEM_TO_BE_SHOWN_IN_CART)
                    tvCartItemsCount.text = count.toString()
                else
                    tvCartItemsCount.text = getString(R.string.text_nine_plus)
            } else {
                tvCartItemsCount.makeGone()
            }
        }
    }


    private fun openWishlist() {
        Intent(this, CustomerBaseActivity::class.java).apply {
            this.putExtra(BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE,
                CustomerHelper.CustomerFragType.TYPE_WISHLIST)
            startActivity(this)
        }
    }

    private fun initDialog() {
        sweetAlertDialog = AlertDialogHelper.getAlertDialog(this,
            SweetAlertDialog.PROGRESS_TYPE, getString(R.string.please_wait),"", false,false);
    }

    private fun navigateToCartScreen() {
        val cartId = AppSharedPref.getCartId(this);
        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE)
            getCartId()
        else {
            startActivity(Intent(this, NewCartActivity::class.java))
        }
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
                        AppSharedPref.setCartId(this@LoyaltyHistoryActivity, response.result.cartId)
                        sweetAlertDialog.dismiss()
                        startActivity(Intent(this@LoyaltyHistoryActivity, NewCartActivity::class.java))
                    }
                }

                override fun onError(t: Throwable) {
                    createCart(customerId)
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
                    AppSharedPref.setCartId(this@LoyaltyHistoryActivity, response.result.cartId)
                    sweetAlertDialog.dismiss()
                    startActivity(Intent(this@LoyaltyHistoryActivity, NewCartActivity::class.java))
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusbarColor() {
        val window: Window = this.window

        //changes the status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
    }


    private fun getSemaaiPoints() {
        ApiConnection.getLoyaltyPoints(this@LoyaltyHistoryActivity, customerId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<ReferralResponse?>(this@LoyaltyHistoryActivity) {
                override fun onNext(response: ReferralResponse) {
                    super.onNext(response)
                    handleLoyaltyPointsResponse(response)
                }
            })
    }

    fun handleLoyaltyPointsResponse(response: ReferralResponse) {
        if (response.status == ApplicationConstant.SUCCESS) {
            binding.tvLoyaltyPoints.text = response.redeemHistory.toString()
        } else {
            SnackbarHelper.getSnackbar(
                this@LoyaltyHistoryActivity,
                response.message,
                Snackbar.LENGTH_LONG,
                SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }

    private fun showLoyaltyPointsDialog(){
        if(AppSharedPref.isLoyaltyPointsDialogActive(this@LoyaltyHistoryActivity)){
            WhatsNewDialogFragment.newInstance().show(
                mSupportFragmentManager,
                WhatsNewDialogFragment::class.java.simpleName
            )
        }
    }

    private fun getLoyaltyPointsHistory(limit: Int, offset: Int) {
        showProgress()
        disablePageButtons()
        ApiConnection.getLoyaltyPointsHistory(this, customerId, limit, offset).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :
                CustomObserver<LoyaltyHistoryResponse>(this) {
                override fun onNext(loyaltyHistoryResponse: LoyaltyHistoryResponse) {
                    super.onNext(loyaltyHistoryResponse)
                    binding.progressBar.visibility = View.GONE
                    loyaltyHistory = loyaltyHistoryResponse.loyaltyTransactions
                    count = loyaltyHistoryResponse.totalCount
                    if (isFirstCall && loyaltyBanners.isNotEmpty())
                        handleFirstCallHistoryResponse(loyaltyHistory)
                    else
                        handlePointsHistoryResponse(loyaltyHistory)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }
            })
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun disablePageButtons() {
        binding.tvNext.isEnabled = false
        binding.tvPrevious.isEnabled = false
    }

    private fun enablePageButtons() {
        if (loyaltyHistory.size == limit && (count != (offset+limit))) {
            enableNextButton()
        } else {
            disableNextButton()
        }
        if (offset == 0) {
            disablePreviousButton()
        } else {
            enablePreviousButton()
        }
    }

    private fun disableNextButton() {
        binding.tvNext.isEnabled = false
        binding.tvNext.setTextColor(resources.getColor(R.color.colorDarkGrey))
        binding.tvNext.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_right_grey,
            0
        );
    }

    private fun enableNextButton() {
        binding.tvNext.isEnabled = true
        binding.tvNext.setTextColor(resources.getColor(R.color.text_color_orange))
        binding.tvNext.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_arrow_right_orange,
            0
        );
    }

    private fun disablePreviousButton() {
        binding.tvPrevious.isEnabled = false
        binding.tvPrevious.setTextColor(resources.getColor(R.color.colorDarkGrey))
        binding.tvPrevious.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_arrow_left_grey,
            0,
            0,
            0
        );
    }

    private fun enablePreviousButton() {
        binding.tvPrevious.isEnabled = true
        binding.tvPrevious.setTextColor(resources.getColor(R.color.text_orange))
        binding.tvPrevious.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_arrow_left_orange,
            0,
            0,
            0
        );
    }

    private fun showMoreHistoryButton() {
        binding.tvMoreHistory.visibility = View.VISIBLE
        binding.vTransparentBg.visibility = View.VISIBLE
    }

    private fun handleFirstCallHistoryResponse(loyaltyHistory: MutableList<LoyaltyTransactionData>) {
        isFirstCall = false
        if (loyaltyHistory.size > miniListSize){
            setView(loyaltyHistory.subList(0, miniListSize))
            showMoreHistoryButton()
        }
        else {
            setView(loyaltyHistory)
        }
    }

    private fun handlePointsHistoryResponse(loyaltyHistory: MutableList<LoyaltyTransactionData>){
        setView(loyaltyHistory)
        hideMoreHistoryButton()
        if(loyaltyHistory.size == limit) {
            showPageButtons()
        }
    }

    private fun showPageButtons() {
        binding.llPages.visibility = View.VISIBLE
        binding.vTopSeparator.visibility = View.VISIBLE
        binding.vBottomSeparator.visibility = View.VISIBLE
    }

    private fun setView(loyaltyHistory: MutableList<LoyaltyTransactionData>) {
        binding.rvLoyaltyTransactions.apply {
            layoutManager = LinearLayoutManager(this@LoyaltyHistoryActivity)
            adapter = LoyaltyHistoryListAdapter(this@LoyaltyHistoryActivity, loyaltyHistory)
        }
        enablePageButtons()
    }

    private fun showLoyaltyhistoryList(loyaltyHistory: MutableList<LoyaltyTransactionData>) {
        setView(loyaltyHistory)
        hideMoreHistoryButton()
        if(loyaltyHistory.size == limit){
            showPageButtons()
        }
    }

    private fun hideMoreHistoryButton() {
        binding.tvMoreHistory.visibility = View.GONE
        binding.vTransparentBg.visibility = View.GONE
    }

    private fun getLoyaltyTerms() {
        showProgress()
        disablePageButtons()
        ApiConnection.getLoyaltyTerms(this).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :
                CustomObserver<LoyaltyTermsResponse>(this) {
                override fun onNext(loyaltyTermsResponse: LoyaltyTermsResponse) {
                    super.onNext(loyaltyTermsResponse)
                    binding.progressBar.visibility = View.GONE
                    loyaltyBanners = loyaltyTermsResponse.termsList
                    handleLoyaltyTermsResponse(loyaltyBanners)
                    getLoyaltyPointsHistory(limit, offset)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    getLoyaltyPointsHistory(limit, offset)
                }

            })
    }

    private fun handleLoyaltyTermsResponse(loyaltyBanners: List<LoyaltyTermsData>){
        if (loyaltyBanners.isEmpty()){
            hideMoreHistoryButton()
        } else {
            showBanners(loyaltyBanners)
        }
    }

    private fun showBanners(loyaltyBanners: List<LoyaltyTermsData>){
        binding.rvLoyaltyTerms.apply {
            layoutManager = LinearLayoutManager(this@LoyaltyHistoryActivity)
            adapter = LoyaltyTermsListAdapter(this@LoyaltyHistoryActivity, loyaltyBanners)
        }
    }


}