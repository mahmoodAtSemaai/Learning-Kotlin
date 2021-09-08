package com.webkul.mobikul.odoo.handler.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.customer.ResetPasswordResponse;
import com.webkul.mobikul.odoo.model.customer.signin.SignInForgetPasswordData;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ForgotPasswordDialogHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "ForgotPasswordDialogHan";
    private final Context mContext;
    private final Dialog mDialog;
    private SignInForgetPasswordData mData;

    public ForgotPasswordDialogHandler(Context context, Dialog dialog, SignInForgetPasswordData data) {
        mContext = context;
        mDialog = dialog;
        mData = data;
    }

    public void forgetPassword() {
        Helper.hideKeyboard(mContext);
        if (mData.getUsername().isEmpty()) {
            SnackbarHelper.getSnackbar((Activity) mContext, "Enter username or email address.", Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        } else {
            AlertDialogHelper.showDefaultProgressDialog(mContext);
            ApiConnection.forgotSignInPassword(mContext
                    , mData.getUsername())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<ResetPasswordResponse>(mContext) {
                        @Override
                        public void onNext(@NonNull ResetPasswordResponse resetPasswordResponse) {
                            super.onNext(resetPasswordResponse);

                            if (resetPasswordResponse.isSuccess()) {
                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(mContext.getString(R.string.link_to_resend_login_password_send))
                                        .setContentText(resetPasswordResponse.getMessage())
                                        .show();
                                dismissDialog();

                            } else {
                                //dismissDialog();
                                SnackbarHelper.getSnackbar((Activity) mContext, resetPasswordResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                            }
                            AlertDialogHelper.dismiss(mContext);
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            AlertDialogHelper.dismiss(mContext);
                        }


                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }
}
