package com.webkul.mobikul.odoo.handler.checkout;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CheckoutActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.BillingShippingFragment;
import com.webkul.mobikul.odoo.fragment.NewAddressFragment;
import com.webkul.mobikul.odoo.fragment.PaymentAcquirerFragment;
import com.webkul.mobikul.odoo.fragment.ShippingMethodFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;

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

public class BillingShippingFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "BillingShippingFragment";
    private Context mContext;
    private MyAddressesResponse mData;

    public BillingShippingFragmentHandler(Context context, MyAddressesResponse myAddressesResponse) {
        mContext = context;
        mData = myAddressesResponse;
    }


    public void editBillingAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(0).getUrl()), mContext.getString(R.string.edit_billing_address)
                , NewAddressFragment.AddressType.TYPE_BILLING), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void editShippingAddress() {
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(BillingShippingFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            int selectedShippingAddressPosn = ((BillingShippingFragment) fragment).mBinding.shippingAddressSpinner.getSelectedItemPosition();
            FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(String.valueOf(mData.getAddresses().get(selectedShippingAddressPosn).getUrl()), mContext.getString(R.string.edit_shipping_address)
                    , NewAddressFragment.AddressType.TYPE_SHIPPING), NewAddressFragment.class.getSimpleName(), true, false);
        }

    }

    public void addNewAddress() {
        FragmentHelper.replaceFragment(R.id.container, mContext, NewAddressFragment.newInstance(null, mContext.getString(R.string.add_new_address), NewAddressFragment.AddressType.TYPE_NEW), NewAddressFragment.class.getSimpleName(), true, false);
    }

    public void loadShippingOrPaymentMethods() {
        if (AppSharedPref.isAllowShipping(mContext)) {

            ApiConnection.getActiveShippings(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<ShippingMethodResponse>(mContext) {

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull ShippingMethodResponse shippingMethodResponse) {
                    super.onNext(shippingMethodResponse);
                    if (shippingMethodResponse.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(mContext);
                                Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((CheckoutActivity)mContext).getClass().getSimpleName());
                                mContext.startActivity(i);
                            }
                        });
                    }else {
                        if (shippingMethodResponse.isSuccess()) {
                            FragmentHelper.addFragment(R.id.container, mContext, ShippingMethodFragment.newInstance(shippingMethodResponse.getShippingMethods()), ShippingMethodFragment.class.getSimpleName(), true, true);
                        } else {
                            Log.d(TAG, "onNext: " + Thread.currentThread());
                            AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), shippingMethodResponse.getMessage());
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

        } else {
            ApiConnection.getPaymentAcquirers(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<PaymentAcquirerResponse>(mContext) {

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull PaymentAcquirerResponse paymentAcquirerResponse) {
                    super.onNext(paymentAcquirerResponse);
                    if (paymentAcquirerResponse.isAccessDenied()){
                        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(mContext);
                                Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((CheckoutActivity)mContext).getClass().getSimpleName());
                                mContext.startActivity(i);
                            }
                        });
                    }else {
                        if (paymentAcquirerResponse.isSuccess()) {
                            FragmentHelper.addFragment(R.id.container, mContext, PaymentAcquirerFragment.newInstance(paymentAcquirerResponse.getPaymentAcquirers()), PaymentAcquirerFragment.class.getSimpleName(), true, true);
                        } else {
                            AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), paymentAcquirerResponse.getMessage());
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
    }
}
