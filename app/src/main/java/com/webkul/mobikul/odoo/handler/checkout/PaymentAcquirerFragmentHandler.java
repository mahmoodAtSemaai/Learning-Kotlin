package com.webkul.mobikul.odoo.handler.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CheckoutActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.BillingShippingFragment;
import com.webkul.mobikul.odoo.fragment.OrderReviewFragment;
import com.webkul.mobikul.odoo.fragment.PaymentAcquirerFragment;
import com.webkul.mobikul.odoo.fragment.ShippingMethodFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse;
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest;

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
public class PaymentAcquirerFragmentHandler {
    @SuppressWarnings("unused")
    private static final String TAG = "PaymentMethodFragmentHa";

    private Context mContext;

    public PaymentAcquirerFragmentHandler(Context context) {
        mContext = context;
    }

    public void savePayment() {
        String selectedShippingMethodId = "";
        String selectedPaymentAcquirerId = "";
        String selectedShippingAddressId = "";
        Fragment fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(PaymentAcquirerFragment.class.getSimpleName());
        if (fragment != null && fragment.isAdded()) {
            PaymentAcquirerFragment paymentAcquirerFragment = (PaymentAcquirerFragment) fragment;
            /*in case no item is selected */
            if (paymentAcquirerFragment.mBinding.paymentAcquirerRg.getCheckedRadioButtonId() == -1) {
                SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.error_no_payment_method_chosen), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                return;
            }
            selectedPaymentAcquirerId = paymentAcquirerFragment.mBinding.paymentAcquirerRg.findViewById(paymentAcquirerFragment.mBinding.paymentAcquirerRg.getCheckedRadioButtonId()).getTag().toString();

            if (AppSharedPref.isAllowShipping(mContext)) {
                Fragment shippingFragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(ShippingMethodFragment.class.getSimpleName());
                if (shippingFragment != null && shippingFragment.isAdded()) {
                    ShippingMethodFragment shippingMethodFragment = (ShippingMethodFragment) shippingFragment;
                    selectedShippingMethodId = shippingMethodFragment.mBinding.shippingMethodRg.findViewById(shippingMethodFragment.mBinding.shippingMethodRg.getCheckedRadioButtonId()).getTag().toString();
                }
            }

            fragment = ((BaseActivity) mContext).mSupportFragmentManager.findFragmentByTag(BillingShippingFragment.class.getSimpleName());
            if (fragment != null && fragment.isAdded()) {
                BillingShippingFragment billingShippingFragment = (BillingShippingFragment) fragment;
                selectedShippingAddressId = billingShippingFragment.mBinding.getData().getAddresses().get(billingShippingFragment.mBinding.shippingAddressSpinner.getSelectedItemPosition()).getAddressId();
            }
        }

        ApiConnection.getOrderReviewData(mContext, new OrderReviewRequest(selectedShippingAddressId, selectedShippingMethodId, selectedPaymentAcquirerId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<OrderReviewResponse>(mContext) {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull OrderReviewResponse orderReviewResponse) {
                super.onNext(orderReviewResponse);
                if (orderReviewResponse.isAccessDenied()){
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
                    if (orderReviewResponse.isSuccess() && orderReviewResponse.getPaymentAcquirerData().isStatus()) {
                        FragmentHelper.addFragment(R.id.container, mContext, OrderReviewFragment.newInstance(orderReviewResponse), OrderReviewFragment.class.getSimpleName(), true, true);
                    } else {
                        SnackbarHelper.getSnackbar((Activity) mContext, orderReviewResponse.getMessage(), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                    }
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}