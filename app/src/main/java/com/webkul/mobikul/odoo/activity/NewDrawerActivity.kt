package com.webkul.mobikul.odoo.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.fragment.AccountFragment
import com.webkul.mobikul.odoo.handler.home.FragmentNotifier
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.model.home.HomePageResponse
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream

class NewDrawerActivity : BaseActivity() {

    private var currentFragmentDisplayed = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_drawer)

        val homePageResponse =
                intent.getParcelableExtra<HomePageResponse>(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE)

        val navController =
                Navigation.findNavController(this@NewDrawerActivity, R.id.drawer_fragment)
        val bundle = Bundle()
        bundle.putParcelable(BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse)
        navController.setGraph(R.navigation.drawer_layout_navigation, bundle)
    }

    override fun onPause() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onPause()
    }

    override fun getScreenTitle(): String = TAG

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFragmentNotifier(currentFragment: FragmentNotifier.HomeActivityFragments?) {
        when (currentFragment) {
            FragmentNotifier.HomeActivityFragments.HOME_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.home)
            FragmentNotifier.HomeActivityFragments.NOTIFICATION_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.notification)
            FragmentNotifier.HomeActivityFragments.ACCOUNT_FRAGMENT -> currentFragmentDisplayed =
                    getString(R.string.account)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            HomeActivity.RC_PICK_IMAGE -> when (resultCode) {
                RESULT_OK -> if (data != null) {
                    val fragment = mSupportFragmentManager.findFragmentById(R.id.drawer_fragment)
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
                RESULT_OK -> if (data != null) {
                    {
                        val fragment = mSupportFragmentManager.findFragmentById(R.id.drawer_fragment)
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
                    }
                } else {
                    SnackbarHelper.getSnackbar(this, getString(R.string.error_in_changing_profile_image), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show()
                }
            }
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> CropImage.activity(CropImage.getPickImageResultUri(this, data))
                    .setGuidelines(CropImageView.Guidelines.ON) //.setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                    .start(this)
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                val fragment = mSupportFragmentManager.findFragmentById(R.id.drawer_fragment)
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

}
