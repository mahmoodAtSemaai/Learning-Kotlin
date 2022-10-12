package com.webkul.mobikul.odoo.handler.customer;

import static com.webkul.mobikul.odoo.BuildConfig.APP_DYNAMIC_LINK;
import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_CAMERA;
import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_PICK_IMAGE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.ChatHistoryActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.NewDrawerActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.AccountFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.ErrorConstants;
import com.webkul.mobikul.odoo.helper.ImageHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.ui.seller.SellerProfileActivityV1;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

    private final Context context;
    private final AccountFragment mAccountFragment;
    private boolean isRequestForProfileImage;
    private String referralMessage;

    public AccountFragmentHandler(Context context, AccountFragment accountFragment) {
        this.context = context;
        mAccountFragment = accountFragment;
    }

    public boolean isRequestForProfileImage() {
        return isRequestForProfileImage;
    }

    public void setRequestForProfileImage(boolean requestForProfileImage) {
        isRequestForProfileImage = requestForProfileImage;
    }

    public void showDashboard() {
        AnalyticsImpl.INSTANCE.trackDashboardSelected();
        Intent intent = new Intent(context, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_DASHBOARD);
        context.startActivity(intent);
    }

    public void showSellerDashboard() {
        Intent intent = new Intent(context, ((OdooApplication) context.getApplicationContext()).getSellerDashBoardPage());
        context.startActivity(intent);
    }

    public void askToAdmin() {
        Intent intent = new Intent(context, ((OdooApplication) context.getApplicationContext()).getAskToAdminActivity());
        context.startActivity(intent);
    }

    public void showSellerProfile() {
        Intent intent = new Intent(context, SellerProfileActivityV1.class);
        intent.putExtra(BUNDLE_KEY_SELLER_ID, Integer.parseInt(AppSharedPref.getCustomerId(context)));
        context.startActivity(intent);
    }

    public void getSellerOrders() {
        Intent intent = new Intent(context, ((OdooApplication) context.getApplicationContext()).getSellerOrdersActivity());
        context.startActivity(intent);
    }

    public void showAccountInfo() {
        AnalyticsImpl.INSTANCE.trackAccountInfoSelected();
        Intent intent = new Intent(context, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ACCOUNT_INFO);
        context.startActivity(intent);
    }

    public void viewAddressBook() {
        AnalyticsImpl.INSTANCE.trackAddressBookSelected();
        Intent intent = new Intent(context, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ADDRESS_BOOK);
        context.startActivity(intent);
    }

    public void viewAllOrder() {
        AnalyticsImpl.INSTANCE.trackAllOrdersSelected();
        Intent intent = new Intent(context, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_ORDER_LIST);
        context.startActivity(intent);
    }

    public void viewWishlist() {
        String sourceScreen = context.getString(R.string.accountScreen);
        AnalyticsImpl.INSTANCE.trackWishlistSelected(sourceScreen);
        Intent intent = new Intent(context, CustomerBaseActivity.class);
        intent.putExtra(BUNDLE_KEY_CUSTOMER_FRAG_TYPE, CustomerHelper.CustomerFragType.TYPE_WISHLIST);
        context.startActivity(intent);
    }

    public void viewReferralCode() {
        showDialogForReferralCode(context, AppSharedPref.getReferralCode(context));
    }

    private void showDialogForReferralCode(Context context, String referralCode) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(context.getString(R.string.referral_code))
                .setContentText(context.getString(R.string.referral_code_is) + ": " + referralCode + "\n\n" + context.getString(R.string.referral_code_share_message))
                .setConfirmText(context.getString(R.string.share_referral_code))
                .setCancelText(context.getString(R.string.cancel_small))
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    referralMessage = createMessageToShare(referralCode);
                    shareReferralCode(referralMessage);
                })
                .setCancelClickListener(Dialog::dismiss).show();
    }

    public String createMessageToShare(String referralCode) {
        String message = context.getString(R.string.lets_use_this_code) + ": " + referralCode + " ";
        message += context.getString(R.string.for_purchasing) + "\n";
        message += APP_DYNAMIC_LINK + " .";
        return message;
    }

    public void shareReferralCode(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.choose_an_action), null));
        } else {
            context.startActivity(sendIntent);
        }
    }

    public void showChatHistory() {
        Intent intent = new Intent(context, ChatHistoryActivity.class);
        context.startActivity(intent);

    }

    public void signOut() {
        AnalyticsImpl.INSTANCE.close();
        ApiConnection.signOut(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(context);
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isSuccess()) {
                    AnalyticsImpl.INSTANCE.trackSignoutSuccess();
                    StyleableToast.makeText(context, context.getString(R.string.have_a_good_day), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                    AppSharedPref.clearCustomerData(context);
                    AppSharedPref.clearUserAnalytics(context);
                    AppSharedPref.setIsAppRunFirstTime(context, false);
                    Intent intent = new Intent(context, SignInSignUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finishAffinity();
                } else {
                    AnalyticsImpl.INSTANCE.trackSignoutFailed(baseResponse.getResponseCode(), baseResponse.getMessage());
                    SnackbarHelper.getSnackbar((Activity) context, baseResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                AnalyticsImpl.INSTANCE.trackSignoutFailed(ErrorConstants.SignOutError.INSTANCE.getErrorCode(), ErrorConstants.SignOutError.INSTANCE.getErrorMessage());
                super.onError(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }



    public void changeProfileImage(boolean isRequestForProfileImage) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity) context).requestPermissions(permissions, RC_CAMERA);
                }
                dialog.dismiss();
            };
            AlertDialogHelper.showPermissionDialog(context, context.getResources().getString(R.string.permission_confirmation), context.getResources().getString(R.string.camra_permission), listener);
            return;
        }


        this.isRequestForProfileImage = isRequestForProfileImage;
        List<String> uploadImageOptions = ImageHelper.getUploadImageOptions(context);
        final CharSequence[] items = uploadImageOptions.toArray(new CharSequence[uploadImageOptions.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        if (isRequestForProfileImage) {
            builder.setTitle(context.getString(R.string.upload_profile_image));
        } else {
            builder.setTitle(context.getString(R.string.upload_banner_image));
        }

        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(context.getString(R.string.choose_from_library))) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromFileSystemIntent();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) context).requestPermissions(permissions, RC_PICK_IMAGE);
                    }
                }
            } else if (items[item].equals(context.getString(R.string.camera_pick))) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickCameraIntent();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) context).requestPermissions(permissions, RC_CAMERA);
                    }
                }

            } else if (items[item].equals(context.getString(R.string.add_image))) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startPickImage();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        ((Activity) context).requestPermissions(permissions, RC_CAMERA);
                    }
                }

            } else if (items[item].equals(context.getString(R.string.cancel))) {
                dialog.dismiss();
            } else if (items[item].equals(context.getString(R.string.delete_my_image))) {
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
//        if (context instanceof HomeActivity) {
//            CropImage.startPickImageActivity((HomeActivity) context);
//        }

        /**
         * Uncomment below code for emulator
         * */
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, context.getString(R.string.select_file));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent, cameraIntent});

        if (context instanceof NewHomeActivity) {
            ((NewHomeActivity) context).startActivityForResult(chooserIntent, RC_PICK_IMAGE);
        } else if (context instanceof NewDrawerActivity) {
            ((NewDrawerActivity) context).startActivityForResult(chooserIntent, RC_PICK_IMAGE);
        }
    }

    public void pickImageFromFileSystemIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_file)), RC_PICK_IMAGE);
    }

    public void pickCameraIntent() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, RC_CAMERA);
    }

    public void startDeleteProfileImageRequest() {
        AlertDialogHelper.showDefaultProgressDialog(context);
        ApiConnection.deleteProfileImage(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(context);
                            Intent i = new Intent(context, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity) context).getClass().getSimpleName());
                            context.startActivity(i);
                        }
                    });
                } else {

                    if (baseResponse.isSuccess()) {
                        AppSharedPref.setCustomerProfileImage(context, "");
                        StyleableToast.makeText(context, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        mAccountFragment.updateProfileImageAfterDelete();
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(context, context.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
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
        AlertDialogHelper.showDefaultProgressDialog(context);
        ApiConnection.deleteBannerImage(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {
            @Override
            public void onNext(BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()) {
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(context);
                            Intent i = new Intent(context, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity) context).getClass().getSimpleName());
                            context.startActivity(i);
                        }
                    });
                } else {

                    if (baseResponse.isSuccess()) {
                        AppSharedPref.setCustomerBannerImage(context, "");

                        StyleableToast.makeText(context, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                        mAccountFragment.updateProfileImageAfterDelete();
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(context, context.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
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
