package com.webkul.mobikul.odoo.handler.customer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.BaseLocationActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_ADD_TO_CART;
import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_BUY_NOW;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_REQ_CODE;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class NewAddressFragHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "NewAddressFragHandler";

    private final Context mContext;
    private final AddressFormResponse mData;
    @Nullable
    private final String mUrl;
    private final NewAddressFragment.AddressType mAddressType;
    private CustomObserver<BaseResponse> mUpdateAddressFormObserver;

    public NewAddressFragHandler(Context context, AddressFormResponse data, @Nullable String url, NewAddressFragment.AddressType addressType) {
        mContext = context;
        mData = data;
        mUrl = url;
        mAddressType = addressType;

        mUpdateAddressFormObserver = new CustomObserver<BaseResponse>(mContext) {
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
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                }else {

                    if (baseResponse.isSuccess()) {

                        ((AppCompatActivity) mContext).getSupportFragmentManager().popBackStack();
                        switch (mAddressType) {
                            case TYPE_BILLING_CHECKOUT:
                                IntentHelper.beginCheckout(mContext);
                                break;
                            default:
                                AlertDialogHelper.showDefaultSuccessDialog(mContext, mContext.getString(R.string.address_book), baseResponse.getMessage());
                                break;
                        }
                    } else {
                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.address_book), baseResponse.getMessage());
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
        };
    }

    public void getCurrentLocation() {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            ((BaseLocationActivity) mContext).startFindLocation();
            ((BaseLocationActivity) mContext).setRequestingLocationUpdates(true);
            ((BaseLocationActivity) mContext).checkLocationSettings();
            dialog.dismiss();
        };
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            AlertDialogHelper.showPermissionDialog(mContext,mContext.getResources().getString(R.string.permission_confirmation),mContext.getResources().getString(R.string.permission_msg),listener);
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
//        builder.setTitle(R.string.guest_user)
//                .setMessage(R.string.error_please_login_to_continue)
//                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    ((BaseLocationActivity) mContext).startFindLocation();
                    ((BaseLocationActivity) mContext).setRequestingLocationUpdates(true);
                    ((BaseLocationActivity) mContext).checkLocationSettings();
//                    dialog.dismiss();
//                })
//                .setNegativeButton(R.string.cancel, (dialog, which) -> {
//                    dialog.dismiss();
//                })
//                .show();

    }else{
            ((BaseLocationActivity) mContext).startFindLocation();
            ((BaseLocationActivity) mContext).setRequestingLocationUpdates(true);
            ((BaseLocationActivity) mContext).checkLocationSettings();
        }

    }

    public void saveAddress() {
        Helper.hideKeyboard((Activity) mContext);
        if (mData.isFormValidated()) {
            switch (mAddressType) {
                case TYPE_BILLING:
                case TYPE_BILLING_CHECKOUT:
                case TYPE_SHIPPING:
                case TYPE_ADDITIONAL:
                    ApiConnection.updateAddressFormData(mContext, mUrl, mData.getNewAddressData()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mUpdateAddressFormObserver);
                    break;
                case TYPE_NEW: {
                //    ApiConnection.addNewAddress(mContext, mData.getNewAddressData()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mUpdateAddressFormObserver);
                }
            }
        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_fill_req_field), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
        }
    }


}
