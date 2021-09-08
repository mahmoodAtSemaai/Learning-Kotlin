package com.webkul.mobikul.odoo.handler.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.dialog_frag.ChangeDefaultShippingDialogFrag;
import com.webkul.mobikul.odoo.fragment.AddressBookFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.dashboard.DashboardData;

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

public class ChangeDefaultShippingDialogFragHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "ChangeDefaultShippingDi";
    private Context mContext;
    private DashboardData mData;
    private Boolean isError;

    public ChangeDefaultShippingDialogFragHandler(Context context, DashboardData data) {
        mContext = context;
        mData = data;
    }

    public void setDefault() {
        Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag(ChangeDefaultShippingDialogFrag.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            ChangeDefaultShippingDialogFrag changeDefaultShippingDialogFrag = (ChangeDefaultShippingDialogFrag) fragment;
            String[] addressUrls = changeDefaultShippingDialogFrag.mBinding.selectShippingRg.findViewById(changeDefaultShippingDialogFrag.mBinding.selectShippingRg.getCheckedRadioButtonId()).getTag().toString().split("/");
            ApiConnection.setDefaultShippingAddress(mContext, addressUrls[addressUrls.length - 1])
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull BaseResponse baseResponse) {
                    super.onNext(baseResponse);
                    isError = !baseResponse.isSuccess();
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
                        SnackbarHelper.getSnackbar((Activity) mContext, baseResponse.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    super.onError(t);
                }

                @Override
                public void onComplete() {
                    Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag(AddressBookFragment.class.getSimpleName());
                    if (fragment != null && fragment.isAdded()) {
                        if (!isError) {
                            mData.updateDefaultShippingAddress(addressUrls[addressUrls.length - 1]);
                        }
                        ((AddressBookFragment) fragment).callApi();
                    }
                    changeDefaultShippingDialogFrag.dismiss();
                }
            });
        }
    }

    public void cancel() {
        Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag(ChangeDefaultShippingDialogFrag.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            ((ChangeDefaultShippingDialogFrag) fragment).dismiss();
        }
    }
}
