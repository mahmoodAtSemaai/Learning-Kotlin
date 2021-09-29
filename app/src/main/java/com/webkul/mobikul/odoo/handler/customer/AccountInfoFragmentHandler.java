package com.webkul.mobikul.odoo.handler.customer;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.activity.SplashScreenActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentAccountInfoBinding;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.account.AccountInforData;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;
import com.webkul.mobikul.odoo.model.request.DeactivateRequest;
import com.webkul.mobikul.odoo.model.request.SaveCustomerDetailRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class AccountInfoFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountInfoFragmentHand";
    private Context mContext;
    private AccountInforData mData;
    public FragmentAccountInfoBinding mBinding;
    String downloadUrl;


    public AccountInfoFragmentHandler(Context context, AccountInforData accountInforData, FragmentAccountInfoBinding binding) {
        mContext = context;
        mData = accountInforData;
        mBinding = binding;

    }

    public void saveAccountInfo() {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (mData.isFormValidated()) {

            ApiConnection.saveCustomerDetails(mContext, new SaveCustomerDetailRequest(mData.getName(), mData.getNewPassword(), null,null)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SaveCustomerDetailResponse>(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull SaveCustomerDetailResponse saveCustomerDetailResponse) {
                    super.onNext(saveCustomerDetailResponse);
                    if (saveCustomerDetailResponse.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(mContext);
                                Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)mContext).getClass().getSimpleName());
                                mContext.startActivity(i);
                            }
                        });
                    }else {
                        if (saveCustomerDetailResponse.isSuccess()) {
                            Toast.makeText(mContext, saveCustomerDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            AppSharedPref.setCustomerName(mContext, mData.getName());
                            if (mData.isChangePassword()) {
                                AppSharedPref.setCustomerLoginBase64Str(mContext, Base64.encodeToString(new AuthenticationRequest(AppSharedPref.getCustomerPhoneNumber(mContext), mData.getNewPassword()).toString().getBytes(), Base64.NO_WRAP).trim());
                            }
                            IntentHelper.continueShopping(mContext);
                        } else {
                            AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), saveCustomerDetailResponse.getMessage());
                        }
                    }
                }

                @Override
                public void onComplete() {

                }
            });

        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_fill_req_field), Snackbar.LENGTH_LONG).show();
        }
    }

    public void emailVerification() {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        ApiConnection.sendEmailVerificationLink(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(mContext);
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse response) {
                        super.onNext(response);
                        if (response.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {

                            if (response.isSuccess()) {
                                SnackbarHelper.getSnackbar((Activity) mContext, response.getMessage(), Snackbar.LENGTH_SHORT).show();
                            } else {
                                AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onClickDeactivate(String type) {
        Log.d(TAG, "DeactivateAcount: " + type);

        ((BaseActivity) mContext).mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getString(R.string.deactivate))
                .setContentText(mContext.getString(R.string.want_to_deactivate_account, type))
                .setConfirmText(mContext.getString(R.string.ok))
                .setConfirmClickListener(sweetAlertDialog -> {
                    deactivateAcount(type);
                });

        ((BaseActivity) mContext).mSweetAlertDialog.show();
        ((BaseActivity) mContext).mSweetAlertDialog.showCancelButton(true);

    }

    private void deactivateAcount(String type) {

        ApiConnection.deactivateAcountDetail(mContext, new DeactivateRequest(type)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(mContext);
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse response) {
                        super.onNext(response);
                        if (response.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity)mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {
                            if (response.isSuccess()) {
                                SnackbarHelper.getSnackbar((Activity) mContext, response.getMessage(), Snackbar.LENGTH_SHORT).show();
                                signOut();

                            } else {
                                AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void signOut() {
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

    public void onClickDownload() {

        ApiConnection.getDownloadData(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);

                if (baseResponse.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(mContext);
                            Intent i = new Intent(mContext, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity)mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                }else {


                    if (baseResponse.isSuccess()) {
                        if (baseResponse.getDownloadMessage() != null) {
                            mBinding.downloadStatus.setVisibility(View.VISIBLE);
                            mBinding.downloadStatus.setText(baseResponse.getDownloadMessage());
                            downloadUrl = baseResponse.getDownloadUrl();
                        }
                        if (baseResponse.isDownloadRequest()) {
                            mBinding.downloadRequest.setVisibility(View.VISIBLE);
                        }
                        if (!baseResponse.isDownloadRequest() && !baseResponse.getDownloadUrl().equals("")) {
                            mBinding.download.setVisibility(View.VISIBLE);
                        }


                    } else {
                        SnackbarHelper.getSnackbar((Activity) mContext, baseResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                    }
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

    public void onClickDownloadRequest() {

        ApiConnection.getDownloadRequestData(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(mContext);
                            Intent i = new Intent(mContext, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity)mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                }else {
                    if (baseResponse.isSuccess()) {
                        SnackbarHelper.getSnackbar((Activity) mContext, baseResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                    } else {
                        SnackbarHelper.getSnackbar((Activity) mContext, baseResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                    }
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

    public void onClickDownloadData() {

        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                dialog.dismiss();
            };
            AlertDialogHelper.showPermissionDialog(mContext, mContext.getResources().getString(R.string.permission_confirmation), mContext.getResources().getString(R.string.download_storage_permission), listener);
        } else {

            String reference = "AccountInfo";
            Toast.makeText(mContext, mContext.getString(R.string.downloading), Toast.LENGTH_LONG).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            request.setDescription(mContext.getResources().getString(R.string.download_started));
            request.setTitle(reference);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, reference);
            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
//            request.setMimeType("application/pdf");
            request.setMimeType("*/*");
            manager.enqueue(request);
        }
    }

}
