package com.webkul.mobikul.odoo.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.helper.AppSharedPref
import com.webkul.mobikul.odoo.helper.ImageHelper
import com.webkul.mobikul.odoo.helper.SnackbarHelper
import com.webkul.mobikul.odoo.ui.signUpOnboarding.UserOnboardingActivity
import com.webkul.mobikul.odoo.ui.signUpOnboarding.fragment.UserDetailsFragment
import java.io.ByteArrayOutputStream
import java.util.*

class CameraV1Utils {

    val RC_PICK_IMAGE = 1004
    val RC_CAMERA = 1005

    fun changeProfileImage(context : Activity) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                "android.permission.CAMERA",
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(permissions, RC_CAMERA)
            }
            /*val listener = DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                val permissions = arrayOf(
                    "android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.requestPermissions(permissions, RC_CAMERA)
                }
                close(context)
                dialog.dismiss()
            }*/
            /*AlertDialogHelper.showPermissionDialog(
                context,
                context.resources.getString(R.string.permission_confirmation),
                context.resources.getString(R.string.camra_permission),
                listener
            )
            close(context)
            return*/
        } else {
            showPictureDialog(context)
        }
        /*val uploadImageOptions: List<String> = getUploadImageOptions(context)
        val items = uploadImageOptions.toTypedArray<CharSequence>()
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        builder.setTitle(context.getString(R.string.upload_profile_image))
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            if (items[item] == context.getString(R.string.choose_from_library)) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromFileSystemIntent(context)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val permissions =
                            arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")
                        context.requestPermissions(
                            permissions,
                            RC_PICK_IMAGE
                        )
                    }
                }
            } else if (items[item] == context.getString(R.string.camera_pick)) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    pickCameraIntent(context)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val permissions = arrayOf(
                            "android.permission.CAMERA",
                            "android.permission.WRITE_EXTERNAL_STORAGE"
                        )
                        context.requestPermissions(
                            permissions,
                            RC_CAMERA
                        )
                    }
                }
            } else if (items[item] == context.getString(R.string.add_image)) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startPickImage(context)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val permissions = arrayOf(
                            "android.permission.CAMERA",
                            "android.permission.WRITE_EXTERNAL_STORAGE"
                        )
                        context.requestPermissions(
                            permissions,
                            RC_CAMERA
                        )
                    }
                }
            } else if (items[item] == context.getString(R.string.cancel)) {
                dialog.dismiss()
                close(context)
            }
        }
        builder.show()*/
    }

    fun startPickImage(context: Activity) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val chooserIntent = Intent.createChooser(getIntent, context.getString(R.string.select_file))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent, cameraIntent))
        context.startActivityForResult(
            chooserIntent,
            RC_PICK_IMAGE
        )
    }

    fun pickImageFromFileSystemIntent(context: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        context.startActivityForResult(
            Intent.createChooser(
                intent,
                context.getString(R.string.select_file)
            ), RC_PICK_IMAGE
        )
    }

    fun pickCameraIntent(context:  Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        context.startActivityForResult(intent, RC_CAMERA)
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        activity: Activity,
        fragment: Fragment
    ) {
        if (requestCode == RC_CAMERA) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val imageData: Uri? =
                    if (data!!.extras != null && data.extras!!["data"] is Bitmap) {
                        getImageUriFromBitmap((data.extras!!["data"] as Bitmap?)!!, activity)
                    } else {
                        data.data
                    }

                CropImage.activity(imageData)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                    .start(activity)
            }
        } else if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (data != null) {
                    try {
                        val imageData: Uri?
                        if (data.extras != null && data.extras!!["data"] is Bitmap) {
                            imageData = (data.extras!!["data"] as Bitmap?)?.let {
                                getImageUriFromBitmap(
                                    it, activity
                                )
                            }
                        } else {
                            imageData = data.data
                        }
                        CropImage.activity(imageData)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setInitialCropWindowPaddingRatio(0f)
                            .start(activity)

                    } catch (e: Exception) {
                        e.printStackTrace()
                        data.data?.let { uploadFile(it, false, activity, fragment) }
                    }
                } else {
                    SnackbarHelper.getSnackbar(
                        activity,
                        activity.getString(R.string.error_in_changing_profile_image),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                }
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
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if(result != null){
                uploadFile(CropImage.getActivityResult(data).getUri(), true, activity, fragment);
            }else{
                close(activity)
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity,
        fragment: Fragment
    ) {
        when (requestCode) {
            RC_CAMERA -> {
                if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPickImage(activity)
                } else {
                    SnackbarHelper.getSnackbar(
                        activity,
                        activity.getString(R.string.error_permision_required_to_change_profile_image),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                }
            }
            RC_PICK_IMAGE -> {
                if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromFileSystemIntent(activity)
                } else {
                    SnackbarHelper.getSnackbar(
                        activity,
                        activity.getString(R.string.error_permision_required_to_change_profile_image),
                        Snackbar.LENGTH_SHORT,
                        SnackbarHelper.SnackbarType.TYPE_WARNING
                    ).show()
                }
            }
        }
    }

    fun uploadFile(
        selectedImageUri: Uri,
        isFromCropImage: Boolean,
        activity: Activity,
        fragment: Fragment
    ) {
        var filePath: String? = ""
        if (!isFromCropImage) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = activity.contentResolver
                .query(selectedImageUri, filePathColumn, null, null, null)
            if (cursor == null) {
                SnackbarHelper.getSnackbar(
                    activity,
                    activity.getString(R.string.error_in_changing_profile_image),
                    Snackbar.LENGTH_SHORT,
                    SnackbarHelper.SnackbarType.TYPE_WARNING
                ).show()
                return
            }
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            filePath = cursor.getString(columnIndex)
            cursor.close()
        } else {
            filePath = selectedImageUri.path
        }

        if (!TextUtils.isEmpty(filePath)) {
            if (filePath != null) {
                AppSharedPref.setCustomerProfileImage(
                    activity,
                    ImageHelper.encodeImage(filePath)
                )
                (fragment as UserDetailsFragment).setImageFromUri(selectedImageUri)
            }
        } else {
            SnackbarHelper.getSnackbar(
                activity,
                activity.getString(R.string.error_in_changing_profile_image),
                Snackbar.LENGTH_SHORT,
                SnackbarHelper.SnackbarType.TYPE_WARNING
            ).show()
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap, activity: Activity): Uri? {
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

    private fun close(context: Activity){
        val fragment = (context as UserOnboardingActivity).supportFragmentManager.findFragmentById(R.id.fl_onboarding_container)
        (fragment as UserDetailsFragment).closeCamera()
    }

    fun showPictureDialog(context: Activity) {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle(context.getString(R.string.select_photo))
        val pictureDialogItems =
            arrayOf(context.getString(R.string.add_image), context.getString(R.string.close))
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> showOptions(context)
                1 -> {
                    dialog.dismiss()
                    close(context)
                }
            }
        }
        pictureDialog.setCancelable(false)
        pictureDialog.show()
    }

    private fun showOptions(context: Activity) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startPickImage(context)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(
                    "android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                )
                context.requestPermissions(
                    permissions,
                    RC_CAMERA
                )
            }
        }
    }
}