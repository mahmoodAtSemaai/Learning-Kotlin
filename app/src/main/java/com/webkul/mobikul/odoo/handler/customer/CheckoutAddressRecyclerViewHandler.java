package com.webkul.mobikul.odoo.handler.customer;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHECKOUT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.activity.UpdateAddressActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CheckoutAddressRecyclerViewHandler {

    private final Context context;
    private final OnAddressDeletedListener addressDeletedListener;
    private AddressData data;

    public CheckoutAddressRecyclerViewHandler(Context context, AddressData additionalAddress, OnAddressDeletedListener onAdditionalAddressDeletedListener) {
        this.context = context;
        data = additionalAddress;
        addressDeletedListener = onAdditionalAddressDeletedListener;
    }

    public void editAddress() {
        context.startActivity(new Intent(context, UpdateAddressActivity.class)
                .putExtra(BUNDLE_KEY_URL, data.getUrl())
                .putExtra(BUNDLE_KEY_NAME, data.getName())
                .putExtra(BUNDLE_KEY_CHECKOUT, true)
                .putExtra(BUNDLE_KEY_NAME, data.getName()));
    }

    public void deleteAddress() {
        ((BaseActivity) context).mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getString(R.string.msg_are_you_sure))
                .setContentText(context.getString(R.string.msg_want_to_delete_this_address))
                .setConfirmText(context.getString(R.string.message_yes_delete_it))
                .setCancelText(context.getString(R.string.cancel))
                .setConfirmClickListener(sDialog -> ApiConnection.deleteAddress(context, data.getUrl()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                sDialog.dismiss();
                                AlertDialogHelper.showDefaultProgressDialog(context);
                            }

                            @Override
                            public void onError(@NonNull Throwable t) {
                                super.onError(t);
                            }

                            @Override
                            public void onNext(@NonNull BaseResponse baseResponse) {
                                super.onNext(baseResponse);
                                if (baseResponse.isAccessDenied()) {
                                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                            AppSharedPref.clearCustomerData(context);
                                            Intent i = new Intent(context, SignInSignUpActivity.class);
                                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity) context).getClass().getSimpleName());
                                            context.startActivity(i);
                                        }
                                    });
                                } else {
                                    if (baseResponse.isSuccess()) {
                                        AlertDialogHelper.showDefaultSuccessDialog(context, context.getString(R.string.message_deleted), baseResponse.getMessage());
                                        addressDeletedListener.onAdditionalAddressDeleted();
                                    } else {
                                        AlertDialogHelper.showDefaultWarningDialog(context, context.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
                );

        ((BaseActivity) context).mSweetAlertDialog.showCancelButton(true);
        ((BaseActivity) context).mSweetAlertDialog.show();


    }

    public interface OnAddressDeletedListener {
        void onAdditionalAddressDeleted();
    }
}
