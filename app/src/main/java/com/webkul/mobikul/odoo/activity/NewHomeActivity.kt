package com.webkul.mobikul.odoo.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.connection.ApiConnection
import com.webkul.mobikul.odoo.connection.CustomObserver
import com.webkul.mobikul.odoo.constant.ApplicationConstant
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.databinding.ActivityNewHomeBinding
import com.webkul.mobikul.odoo.fragment.AccountFragment
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier.HomeActivityFragments
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.CartUpdateListener
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.ReferralResponse
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper.isChatFeatureEnabled
import io.reactivex.android.schedulers.AndroidSchedulers
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
        openDrawer()
        getBagItemsCount()
        getLoyaltyPoints()

        binding.searchView.setOnClickListener {
            binding.materialSearchView.visibility = View.VISIBLE
            binding.materialSearchView.openSearch()
        }

        binding.cartIcon.setOnClickListener {
            startActivity(Intent(this@NewHomeActivity, BagActivity::class.java))
        }

        binding.ivChatIcon.setOnClickListener { showChatHistory() }
    }

    override fun onBackPressed() {
        if (binding.materialSearchView.isVisible) {
            binding.materialSearchView.visibility = View.GONE
            binding.materialSearchView.closeSearch()
        } else
            super.onBackPressed()
    }

    private fun openDrawer() {
        binding.drawerIcon.setOnClickListener {
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
        val count = AppSharedPref.getCartCount(this@NewHomeActivity, 0)
        binding.apply {
        if (count != 0) {
                badgeInfo.makeVisible()
                if (count < 10)
                    badgeInfo.text = count.toString()
                else
                    badgeInfo.text = getString(R.string.text_nine_plus)
            } else {
                badgeInfo.makeGone()
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
        if (chatUnreadMessageCount > 0) {
            binding.ivUnreadChatCount.text =
                    if (chatUnreadMessageCount > 9) getString(R.string.text_nine_plus)
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
            binding.ivChatIcon.visibility = View.VISIBLE
        } else {
            binding.ivChatIcon.visibility = View.INVISIBLE
            binding.ivUnreadChatCount.visibility = View.INVISIBLE
        }
    }

    override fun updateCart() {
        getBagItemsCount()
    }
}