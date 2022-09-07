package com.webkul.mobikul.odoo.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeInvisible
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.core.utils.HTTP_RESOURCE_NOT_FOUND
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse
import com.webkul.mobikul.odoo.data.response.models.GetCartId
import com.webkul.mobikul.odoo.databinding.ActivityNewHomeBinding
import com.webkul.mobikul.odoo.databinding.ItemUserApprovalDialogBinding
import com.webkul.mobikul.odoo.fragment.AccountFragment
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier.HomeActivityFragments
import com.webkul.mobikul.odoo.helper.AlertDialogHelper
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.CartUpdateListener
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.helper.*
import com.webkul.mobikul.odoo.model.BaseResponse
import com.webkul.mobikul.odoo.model.ReferralResponse
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.ui.cart.NewCartActivity
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper.isChatFeatureEnabled
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream


class NewHomeActivity : BaseActivity(), CartUpdateListener {
    lateinit var binding: ActivityNewHomeBinding
    private val RC_ACCESS_FINE_LOCATION_NEW_ADDRESS = 1001
    private val RC_CHECK_LOCATION_SETTINGS = 1003
    private val RC_PICK_IMAGE = 1004
    private val RC_CAMERA = 1005
    private val RC_SIGN_IN_SIGN_UP = 1006
    private val TAG = "NewHomeActivity"
    lateinit var navController: NavController
    private val mBackPressedTime: Long = 0
    private var currentFragmentDisplayed = ""
    private var unreadChatCount = 0
    lateinit var sweetAlertDialog : SweetAlertDialog

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = this.window

        //changes the status bar color
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_appbar_color)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_home)
        setupUIController()

        //Open the Navigation Drawer
        initDialog()
        openDrawer()
        getBagItemsCount()
        getLoyaltyPoints()

        //RegisterFCMToken
        registerFCMToken()

        setCartId()

        binding.searchView.setOnClickListener {
            binding.materialSearchView.visibility = View.VISIBLE
            binding.materialSearchView.openSearch()
        }

        binding.ivCartIcon.setOnClickListener {
            navigateToCartActivity()
        }

        binding.ivChatIcon.setOnClickListener { showChatHistory() }

        binding.rlSemaaiPointsContainer.setOnClickListener() {
            showLoyaltyPointsHistory()
        }
        if (!AppSharedPref.getUserIsApproved(applicationContext)) {
            showApprovalDialog()
        }
    }

    private fun showApprovalDialog() {
        val dialog = Dialog(this@NewHomeActivity)
        val dialogBinding: ItemUserApprovalDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this@NewHomeActivity),
            R.layout.item_user_approval_dialog,
            null,
            false
        )
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnSignOut.setOnClickListener {
            dialog.dismiss()
            signOut()
        }
        dialogBinding.clWhatsappNo.setOnClickListener {
            IntentHelper.goToWhatsApp(this)
        }
    }

    private fun signOut() {
        ApiConnection.signOut(this).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CustomObserver<BaseResponse?>(this) {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    AlertDialogHelper.showDefaultProgressDialog(this@NewHomeActivity)
                }

                override fun onNext(baseResponse: BaseResponse) {
                    super.onNext(baseResponse)
                    if (baseResponse.isSuccess) {
                        StyleableToast.makeText(
                            this@NewHomeActivity,
                            baseResponse.message,
                            Toast.LENGTH_SHORT,
                            R.style.GenericStyleableToast
                        ).show()
                        AppSharedPref.clearCustomerData(this@NewHomeActivity)
                        val intent = Intent(
                            this@NewHomeActivity,
                            SignInSignUpActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        this@NewHomeActivity.startActivity(intent)
                        finish()
                    } else {
                        SnackbarHelper.getSnackbar(
                            (this@NewHomeActivity as Activity),
                            baseResponse.message,
                            Snackbar.LENGTH_LONG,
                            SnackbarHelper.SnackbarType.TYPE_WARNING
                        ).show()
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                }

                override fun onComplete() {}
            })
    }

    private fun initDialog() {
        sweetAlertDialog = AlertDialogHelper.getAlertDialog(this,
            SweetAlertDialog.PROGRESS_TYPE, getString(R.string.please_wait),"", false,false)
    }

    private fun setCartId(){
        val cartId = AppSharedPref.getCartId(this)

        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE) {
            callGetCartApi()
        }
    }

    private fun callGetCartApi(){
        val customerId = AppSharedPref.getCustomerId(this).toInt()
        ApiConnection.checkIfCartExists(this, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){
                    override fun onNext(response: CartBaseResponse<GetCartId>) {
                        super.onNext(response)
                        if(response.statusCode == HTTP_RESOURCE_NOT_FOUND)
                        else {
                            AppSharedPref.setCartId(this@NewHomeActivity, response.result.cartId)
                        }
                    }

                    override fun onError(t: Throwable) {
                    }
                })
    }

    private fun callCreateCartApi(customerId: Int) {
        ApiConnection.createCart(this, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<CartBaseResponse<GetCartId>>(this){

                    override fun onNext(response: CartBaseResponse<GetCartId>) {
                        super.onNext(response)
                        AppSharedPref.setCartId(this@NewHomeActivity, response.result.cartId)
                    }

                    override fun onError(t: Throwable) {

                    }
                })
    }

    private fun navigateToCartActivity() {
        val cartId = AppSharedPref.getCartId(this)
        if(cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE) {
            getCartId()
        }
        else {
            startNewCartActivity()
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
                        AppSharedPref.setCartId(this@NewHomeActivity, response.result.cartId)
                        sweetAlertDialog.hide()
                        startNewCartActivity()
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
                    AppSharedPref.setCartId(this@NewHomeActivity, response.result.cartId)
                    sweetAlertDialog.dismiss()
                    startNewCartActivity()
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    sweetAlertDialog.dismiss()
                    SnackbarHelper.getSnackbar(this@NewHomeActivity, t.message,Snackbar.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                    super.onComplete()
                }
            })
    }

    private fun startNewCartActivity() {
        startActivity(Intent(this, NewCartActivity::class.java)
            .putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, getHomePageResponse()))
    }

    override fun onBackPressed() {
        if (binding.materialSearchView.isVisible) {
            binding.materialSearchView.visibility = View.GONE
            binding.materialSearchView.closeSearch()
        }
        /*
        else
            super.onBackPressed()
        */
    }

    private fun showLoyaltyPointsHistory() {
        Intent(this, LoyaltyHistoryActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun openDrawer() {
        binding.ivDrawerIcon.setOnClickListener {
            val intent = Intent(this@NewHomeActivity, NewDrawerActivity::class.java)
            intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, getHomePageResponse())
            startActivity(intent)
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
    }

    override fun getScreenTitle(): String = TAG

    fun getLoyaltyPoints() {
        hitApiForLoyaltyPoints(AppSharedPref.getCustomerId(this@NewHomeActivity))
    }

    fun hitApiForLoyaltyPoints(userId: String?) {
        ApiConnection.getLoyaltyPoints(this@NewHomeActivity, userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<ReferralResponse?>(this@NewHomeActivity) {
                    override fun onNext(response: ReferralResponse) {
                        super.onNext(response)
                        handleLoyaltyPointsResponse(response)
                    }
                })
    }

    fun handleLoyaltyPointsResponse(response: ReferralResponse) {
        if (response.status == ApplicationConstant.SUCCESS) {
            showPoints(response.redeemHistory)
        } else {
            SnackbarHelper.getSnackbar(
                    (this@NewHomeActivity as Activity?)!!,
                    response.message,
                    Snackbar.LENGTH_LONG,
                    SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }

    fun showPoints(loyaltyPoints: Int) {
        binding.loyaltyPoints.text = loyaltyPoints.toString()
    }

    private fun setupUIController() {
        navController = Navigation.findNavController(this, R.id.home_nav_host)
        val bottomNavigationView: BottomNavigationView = binding.homeBottomNav
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    fun getHomePageResponse(): HomePageResponse? =
            intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFragmentNotifier(currentFragment: HomeActivityFragments?) {
        when (currentFragment) {
            HomeActivityFragments.HOME_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.home)
            HomeActivityFragments.NOTIFICATION_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.notification)
            HomeActivityFragments.ACCOUNT_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.account)
        }
    }

    override fun onResume() {
        getBagItemsCount()
        getLoyaltyPoints()
        checkChat()
        super.onResume()
    }

    private fun checkChat() {
        if (isChatFeatureEnabled) {
            setChatButton(true)
            fetchUnreadChatCount()
        } else {
            setChatButton(false)
        }
    }

    private fun getBagItemsCount() {
        val count = AppSharedPref.getNewCartCount(this@NewHomeActivity)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            HomeActivity.RC_PICK_IMAGE -> when (resultCode) {
                RESULT_OK -> if (data != null) {
                    val fragment = mSupportFragmentManager.findFragmentById(R.id.home_nav_host)
                    if (fragment != null && fragment.isAdded) {
                        try {
                            val imageData: Uri? = if (data.extras != null && data.extras!!["data"] is Bitmap) {
                                getImageUriFromBitmap(data.extras!!["data"] as Bitmap)
                            } else {
                                data.data
                            }
                            CropImage.activity(imageData)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setAspectRatio(1, 1)
                                    .setInitialCropWindowPaddingRatio(0f)
                                    .start(this)

                        } catch (e: Exception) {
                            e.printStackTrace()
                            (fragment.childFragmentManager.fragments[0] as AccountFragment).uploadFile(data.data, false)
                        }
                    }
                } else {
                    SnackbarHelper.getSnackbar(this, getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }
            }
            HomeActivity.RC_CAMERA -> when (resultCode) {
                RESULT_OK -> {
                    if (data != null) {
                        val fragment = mSupportFragmentManager.findFragmentById(R.id.home_nav_host)
                        if (fragment != null && fragment.isAdded) {
                            val imageData: Uri?
                            if (data!!.extras != null && data.extras!!["data"] is Bitmap) {
                                imageData = getImageUriFromBitmap(data.extras!!["data"] as Bitmap)
                            } else {
                                imageData = data.data
                            }
                            CropImage.activity(imageData)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setAspectRatio(1, 1)
                                    .setInitialCropWindowPaddingRatio(0f)
                                    .start(this)
                        }
                    } else {
                        SnackbarHelper.getSnackbar(this, getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                    }
                }
            }
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> CropImage.activity(CropImage.getPickImageResultUri(this, data))
                    .setGuidelines(CropImageView.Guidelines.ON) //.setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                    .start(this)
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                val fragment = mSupportFragmentManager.findFragmentById(R.id.home_nav_host)
                if (fragment != null && fragment.isAdded) {
                    (fragment.childFragmentManager.fragments[0] as AccountFragment).uploadFile(CropImage.getActivityResult(data).uri, true)
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun fetchUnreadChatCount() {
        ApiConnection.getUnreadChatCount(this).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CustomObserver<ChatBaseResponse<*>?>(this) {
                    override fun onNext(chatUnreadMessageCountChatBaseResponse: ChatBaseResponse<*>) {
                        super.onNext(chatUnreadMessageCountChatBaseResponse)
                        setUnreadChatCount(chatUnreadMessageCountChatBaseResponse)
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                    }
                })
    }

    private fun setUnreadChatCount(chatUnreadMessageCountChatBaseResponse: ChatBaseResponse<*>) {
        val chatUnreadMessageCount =
                chatUnreadMessageCountChatBaseResponse.unreadMessagesCount
        if (chatUnreadMessageCount > ApplicationConstant.MIN_UNREAD_CHAT_COUNT) {
            binding.ivUnreadChatCount.text =
                    if (chatUnreadMessageCount > ApplicationConstant.MAX_UNREAD_CHAT_COUNT) getString(R.string.text_nine_plus)
                    else chatUnreadMessageCount.toString()
            binding.ivUnreadChatCount.visibility = View.VISIBLE
        } else {
            binding.ivUnreadChatCount.visibility = View.INVISIBLE
        }
    }

    private fun showChatHistory() {
        Intent(this, ChatHistoryActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun setChatButton(isChatEnable: Boolean) {
        if (isChatEnable) {
            binding.ivChatIcon.makeVisible()
        } else {
            binding.ivChatIcon.makeInvisible()
            binding.ivUnreadChatCount.makeInvisible()
        }
    }

    private fun registerFCMToken() {
        if (AppSharedPref.getCustomerId(this).isNotBlank() and AppSharedPref.isFCMTokenSynced(this).not()) {
            ApiConnection.registerDeviceToken(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : CustomObserver<BaseResponse>(this) {
                override fun onNext(baseResponse: BaseResponse) {
                    super.onNext(baseResponse)
                    AppSharedPref.setFcmTokenSynced(this@NewHomeActivity, baseResponse.isSuccess)
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    AppSharedPref.setFcmTokenSynced(this@NewHomeActivity, false)
                }
            })
        }
    }
    override fun updateCart() {
        getBagItemsCount()
    }
}