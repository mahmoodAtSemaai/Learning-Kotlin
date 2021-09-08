package com.webkul.mobikul.odoo.handler.checkout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CheckoutActivity;
import com.webkul.mobikul.odoo.activity.OrderPlacedActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse;
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.ACQUIRER_CODE_STRIPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_URL;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderReviewFragmentHandler {

    @SuppressWarnings("unused")
    private static final String TAG = "OrderReviewFragmentHand";
    private Context mContext;
    private OrderReviewResponse mData;
    private boolean isTermAndCondition = false;

    public OrderReviewFragmentHandler(Context context, OrderReviewResponse orderReviewResponse) {
        mContext = context;
        mData = orderReviewResponse;
    }


    public void placeOrder() {

        if (AppSharedPref.isTermAndCondition(mContext)) {
            if (isTermAndCondition) {
                placeOrderMethod();

            } else {
                SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
            }
        } else {
            placeOrderMethod();
        }
    }


    public void placeOrderMethod() {
            ApiConnection.placeOrder(mContext, new PlaceOrderRequest(mData.getTransactionId())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<OrderPlaceResponse>(mContext) {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    super.onSubscribe(d);
                    AlertDialogHelper.showDefaultProgressDialog(mContext);
                }

                @Override
                public void onNext(@NonNull OrderPlaceResponse orderPlaceResponse) {
                    super.onNext(orderPlaceResponse);
                    if (orderPlaceResponse.isAccessDenied()){
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
                        if (orderPlaceResponse.isSuccess()) {
                            Intent intent = new Intent(mContext, OrderPlacedActivity.class);
                            intent.putExtra(BUNDLE_KEY_URL, orderPlaceResponse.getUrl());
                            intent.putExtra(BUNDLE_KEY_ORDER_NAME, orderPlaceResponse.getName());
                            mContext.startActivity(intent);
                            ((Activity) mContext).finish();
                        } else {
                            AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(R.string.error), orderPlaceResponse.getMessage(), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    IntentHelper.goToBag(mContext);
                                    if (mContext instanceof CheckoutActivity) {
                                        ((CheckoutActivity) mContext).finish();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onComplete() {

                }
            });

    }

    public void viewTermNCond() {
        LinearLayout addedLayout = new LinearLayout(mContext);
        addedLayout.setOrientation(LinearLayout.VERTICAL);
        WebView myWebView = new WebView(mContext);

        Helper.enableDarkModeInWebView(mContext, myWebView);

        String mime = "text/html";
        String encoding = "utf-8";
        myWebView.loadDataWithBaseURL("", mData.getPaymentTerms().getPaymentLongTerms(), mime, encoding, "");
        addedLayout.addView(myWebView);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setView(addedLayout);
        dialog.show();
    }

    public boolean isTermAndCondition() {
        return isTermAndCondition;
    }

    public void setTermAndCondition(boolean termAndCondition) {
        isTermAndCondition = termAndCondition;
    }

}
