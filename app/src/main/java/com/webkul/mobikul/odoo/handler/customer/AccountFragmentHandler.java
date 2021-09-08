package com.webkul.mobikul.odoo.handler.customer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.activity.SplashScreenActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.AccountFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.ImageHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_CAMERA;
import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_PICK_IMAGE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class AccountFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountFragmentHandler";

    private final Context mContext;
    private final AccountFragment mAccountFragment;
    private boolean isRequestForProfileImage;

    public AccountFragmentHandler(Context context, AccountFragment accountFragment) {
        mContext = context;
        mAccountFragment = accountFragment;
    }

    public boolean isRequestForProfileImage() {
        return isRequestForProfileImage;
    }

    public void setRequestForProfileImage(boolean requestForProfileImage) {
        isRequestForProfileImage = requestForProfileImage;
    }

    public void showDashboard() {
        Intent intent = new Intent(mContext, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_DASHBOARD);
        mContext.startActivity(intent);
    }

    public void showSellerDashboard() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getSellerDashBoardPage());
        mContext.startActivity(intent);
    }

    public void askToAdmin() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getAskToAdminActivity());
        mContext.startActivity(intent);
    }

    public void showSellerProfile() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getSellerProfileActivity());
        intent.putExtra(BUNDLE_KEY_SELLER_ID, AppSharedPref.getCustomerId(mContext));
        mContext.startActivity(intent);
    }

    public void getSellerOrders() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getSellerOrdersActivity());
        mContext.startActivity(intent);
    }

    public void showAccountInfo() {
        Intent intent = new Intent(mContext, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ACCOUNT_INFO);
        mContext.startActivity(intent);
    }

    public void viewAddressBook() {
        Intent intent = new Intent(mContext, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ADDRESS_BOOK);
        mContext.startActivity(intent);
    }

    public void viewAllOrder() {
        Intent intent = new Intent(mContext, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ORDER_LIST);
        mContext.startActivity(intent);
    }

    public void viewWishlist() {
        Intent intent = new Intent(mContext, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_WISHLIST);
        mContext.startActivity(intent);
    }

    public void signOut() {
        ApiConnection.signOut(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isSuccess()) {
                    StyleableToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                    AppSharedPref.clearCustomerData(mContext);
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                } else {
                    SnackbarHelper.getSnackbar((Activity) mContext, baseResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void changeProfileImage(boolean isRequestForProfileImage) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) mContext).requestPermissions(permissions, RC_CAMERA);
                }
                dialog.dismiss();
            };
            AlertDialogHelper.showPermissionDialog(mContext, mContext.getResources().getString(R.string.permission_confirmation), mContext.getResources().getString(R.string.camra_permission), listener);
            return;
        }


        this.isRequestForProfileImage = isRequestForProfileImage;
        List<String> uploadImageOptions = ImageHelper.getUploadImageOptions(mContext);
        final CharSequence[] items = uploadImageOptions.toArray(new CharSequence[uploadImageOptions.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        if (isRequestForProfileImage) {
            builder.setTitle(mContext.getString(R.string.upload_profile_image));
        } else {
            builder.setTitle(mContext.getString(R.string.upload_banner_image));
        }

        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(mContext.getString(R.string.choose_from_library))) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromFileSystemIntent();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) mContext).requestPermissions(permissions, RC_PICK_IMAGE);
                    }
                }
            } else if (items[item].equals(mContext.getString(R.string.camera_pick))) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickCameraIntent();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) mContext).requestPermissions(permissions, RC_CAMERA);
                    }
                }

            } else if (items[item].equals(mContext.getString(R.string.add_image))) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startPickImage();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) mContext).requestPermissions(permissions, RC_CAMERA);
                    }
                }

            } else if (items[item].equals(mContext.getString(R.string.cancel))) {
                dialog.dismiss();
            } else if (items[item].equals(mContext.getString(R.string.delete_my_image))) {
                dialog.dismiss();
                if (isRequestForProfileImage) {
                    startDeleteProfileImageRequest();
                } else {
                    startDeleteBannerImageRequest();
                }

            }
        });
        builder.show();
    }

    public void showEnlargedProfileImage() {
    }

    public void startPickImage() {
//        if (mContext instanceof HomeActivity) {
//            CropImage.startPickImageActivity((HomeActivity) mContext);
//        }

        /**
         * Uncomment below code for emulator
         * */
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, mContext.getString(R.string.select_file));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent, cameraIntent});

        ((HomeActivity) mContext).startActivityForResult(chooserIntent, RC_PICK_IMAGE);
    }

    public void pickImageFromFileSystemIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*");
        ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.select_file)), RC_PICK_IMAGE);
    }

    public void pickCameraIntent() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) mContext).startActivityForResult(intent, RC_CAMERA);
    }

    public void startDeleteProfileImageRequest() {
        AlertDialogHelper.showDefaultProgressDialog(mContext);
        ApiConnection.deleteProfileImage(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(mContext);
                            Intent i = new Intent(mContext, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity) mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                } else {

                    if (baseResponse.isSuccess()) {
                        AppSharedPref.setCustomerProfileImage(mContext, "");
                        StyleableToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        mAccountFragment.updateProfileImageAfterDelete();
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }
        });

    }

    public void startDeleteBannerImageRequest() {
        AlertDialogHelper.showDefaultProgressDialog(mContext);
        ApiConnection.deleteBannerImage(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(mContext);
                            Intent i = new Intent(mContext, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity) mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                } else {

                    if (baseResponse.isSuccess()) {
                        AppSharedPref.setCustomerBannerImage(mContext, "");

                        StyleableToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        mAccountFragment.updateProfileImageAfterDelete();
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }
        });

    }


}
