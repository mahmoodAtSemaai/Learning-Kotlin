package com.webkul.mobikul.odoo.handler.checkout;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.PaymentAcquirerFragment;
import com.webkul.mobikul.odoo.fragment.ShippingMethodFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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

public class ShippingMethodFragmentHandler {
    private static final String TAG = "SHIPPING_MEHOD_FRAGMENT";
    private Context mContext;

    public ShippingMethodFragmentHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void saveShipping() {
        Fragment shippingFragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(ShippingMethodFragment.class.getSimpleName());
        if (shippingFragment != null && shippingFragment.isAdded()) {
            ShippingMethodFragment shippingMethodFragment = (ShippingMethodFragment) shippingFragment;
            /*in case no item is selected */
            if (shippingMethodFragment.mBinding.shippingMethodRg.getCheckedRadioButtonId() == -1) {
                SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_no_shipping_method_chosen), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                return;
            }
        }
        ApiConnection.getPaymentAcquirers(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<PaymentAcquirerResponse>(mContext) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull PaymentAcquirerResponse paymentAcquirerResponse) {
                super.onNext(paymentAcquirerResponse);
                if (paymentAcquirerResponse.isSuccess()) {
                    FragmentHelper.addFragment(R.id.container, mContext, PaymentAcquirerFragment.newInstance(paymentAcquirerResponse.getPaymentAcquirers()), PaymentAcquirerFragment.class.getSimpleName(), true, true);
                } else {
                    Log.d(TAG, "onNext: " + Thread.currentThread());
                    AlertDialogHelper.showDefaultWarningDialog(mContext, mContext.getString(R.string.error_something_went_wrong), paymentAcquirerResponse.getMessage());
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
