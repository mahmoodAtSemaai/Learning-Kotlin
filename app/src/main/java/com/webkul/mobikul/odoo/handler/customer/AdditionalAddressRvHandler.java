package com.webkul.mobikul.odoo.handler.customer;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;

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
public class AdditionalAddressRvHandler {

    private final Context mContext;
    private final OnAdditionalAddressDeletedListener mOnAdditionalAddressDeletedListener;
    private AddressData mData;

    public AdditionalAddressRvHandler(Context context, AddressData additionalAddress, OnAdditionalAddressDeletedListener onAdditionalAddressDeletedListener) {
        mContext = context;
        mData = additionalAddress;
        mOnAdditionalAddressDeletedListener = onAdditionalAddressDeletedListener;
    }

    public void editAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getUrl()), mContext.getString(R.string.additional_address)
                , NewAddressFragment.AddressType.TYPE_ADDITIONAL), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void deleteAddress() {
        ((BaseActivity) mContext).mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getString(R.string.msg_are_you_sure))
                .setContentText(mContext.getString(R.string.msg_want_to_delete_this_address))
                .setConfirmText(mContext.getString(R.string.message_yes_delete_it))
                .setCancelText(mContext.getString(R.string.cancel))
                .setConfirmClickListener(sDialog -> ApiConnection.deleteAddress(mContext, mData.getUrl()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                sDialog.dismiss();
                                AlertDialogHelper.showDefaultProgressDialog(mContext);
                            }

                            @Override
                            public void onError(@NonNull Throwable t) {
                                super.onError(t);
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
                                        AlertDialogHelper.showDefaultSuccessDialog(mContext, mContext.getString(R.string.message_deleted), baseResponse.getMessage());
                                        mOnAdditionalAddressDeletedListener.onAdditionalAddressDeleted();
                                    } else {
                                        AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
                );

        ((BaseActivity) mContext).mSweetAlertDialog.showCancelButton(true);
        ((BaseActivity) mContext).mSweetAlertDialog.show();


    }

    public interface OnAdditionalAddressDeletedListener {
        void onAdditionalAddressDeleted();
    }
}
