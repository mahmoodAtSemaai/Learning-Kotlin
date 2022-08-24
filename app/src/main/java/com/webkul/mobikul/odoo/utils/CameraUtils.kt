package com.webkul.mobikul.odoo.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment.UserDetailsFragment
import java.io.ByteArrayOutputStream
import java.util.*

class CameraUtils {
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2

    fun checkCameraPermission(context: Activity) {
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        } else {
            showPictureDialog(context)
        }
    }

    fun showPictureDialog(context: Activity) {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle(context.getString(R.string.select_photo))
        val pictureDialogItems =
            arrayOf(context.getString(R.string.select_from_gallery), context.getString(R.string.select_from_camera), context.getString(R.string.close))
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> openGallery(context)
                1 -> openCamera(context)
                2 -> {
                    dialog.dismiss()
                    close(context)
                }
            }
        }
        pictureDialog.setCancelable(false)
        pictureDialog.show()
    }

    private fun close(context: Activity){
        val fragment = (context as UserOnboardingActivity).supportFragmentManager.findFragmentById(R.id.fl_onboarding_container)
        (fragment as UserDetailsFragment).closeCamera()
    }

    private fun openCamera(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun openGallery(activity: Activity) {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(activity.packageManager)?.also {
                activity.startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }

    fun getImageUriFromBitmap(bitmap: Bitmap, activity: Activity): Uri? {
        val imgOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgOutputStream)
        val imgSaved: String = MediaStore.Images.Media.insertImage(
            activity.contentResolver,
            bitmap,
            UUID.randomUUID().toString() + ".jpeg",
            ""
        )
        return Uri.parse(imgSaved)
    }

    fun getBase64String(uri: Uri, activity: Activity): String {
        val bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
        return convertToBase64(bitmap)
    }

    private fun convertToBase64(bitmap: Bitmap): String {
        val imgOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgOutputStream)
        val image = imgOutputStream.toByteArray()
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity, fragment: Fragment) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val uri = getImageUriFromBitmap(bitmap, activity)
                if(uri == null){
                    Toast.makeText(activity,activity.getString(R.string.error_something_went_wrong),
                        Toast.LENGTH_LONG).show()
                }else{
                    CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setInitialCropWindowPaddingRatio(0f)
                        .start(activity)
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.data
                if (uri != null) {
                    CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setInitialCropWindowPaddingRatio(0f)
                        .start(activity)
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val uri = CropImage.getActivityResult(data).uri
                (fragment as UserDetailsFragment).setImageFromUri(uri)
            }
        } else if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            CropImage.activity(
                CropImage.getPickImageResultUri(
                    activity,
                    data
                )
            ).setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0f)
                .start(activity)
        }
    }
}