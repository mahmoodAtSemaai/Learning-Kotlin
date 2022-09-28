package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_LOYALTY_BANNER_ID
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper
import com.webkul.mobikul.odoo.databinding.ActivityLoyaltyTermsBinding
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.CustomerHelper
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsData
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LoyaltyTermsActivity : BaseActivity() {

    val TAG = "LoyaltyTermsActivity"
    private lateinit var binding: ActivityLoyaltyTermsBinding
    private var bannerId: Int = 0
    var loyaltyBanner: LoyaltyTermsData = LoyaltyTermsData()


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_loyalty_terms)
        changeStatusbarColor()
        getBannerId()
        getBannerData()
        setIconOnClicks()
    }

    override fun getScreenTitle(): String {
        return TAG
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusbarColor() {
        val window: Window = this.window

        //changes the status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)
    }

    private fun setIconOnClicks() {
        binding.btnShopping.setOnClickListener {
            startShopping()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startShopping() {
        Intent(this, NewHomeActivity::class.java).apply {
            this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun getBannerId() {
        if (intent.hasExtra(BUNDLE_KEY_LOYALTY_BANNER_ID)) {
            bannerId = intent.getIntExtra(BUNDLE_KEY_LOYALTY_BANNER_ID, 0)
        }
    }

    private fun getBannerData() {
        binding.progressBar.visibility = View.VISIBLE
        ApiConnection.getLoyaltyTermDetails(this, bannerId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :
                CustomObserver<LoyaltyTermsResponse>(this) {
                override fun onNext(loyaltyTermsResponse: LoyaltyTermsResponse) {
                    super.onNext(loyaltyTermsResponse)
                    binding.progressBar.visibility = View.GONE
                    loyaltyBanner = loyaltyTermsResponse.terms
                    handleLoyaltyTermsResponse(loyaltyBanner)
                }
            })
    }

    private fun handleLoyaltyTermsResponse(banner: LoyaltyTermsData) {
        binding.data = banner
        showImage(banner.imageUrl)
        showTerms(banner)
        showHowtoUse(banner)
    }

    private fun showHowtoUse(banner: LoyaltyTermsData) {
        binding.tvHowToUse.text = banner.howToUse
    }

    private fun showTerms(banner: LoyaltyTermsData) {
        binding.tvTerms.text = banner.terms
    }

    private fun showImage(imageUrl: String){
        val imageByteArray: ByteArray = Base64.decode(imageUrl, Base64.DEFAULT)
        Glide.with(this@LoyaltyTermsActivity)
            .asBitmap()
            .load(imageByteArray)
            .into(binding.ivLoyaltyBanner)
    }

}