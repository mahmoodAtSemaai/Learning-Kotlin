package com.webkul.mobikul.odoo.handler.bag;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BagActivity;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.dialog_frag.ChangeQtyDialogFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.cart.BagItemData;
import com.webkul.mobikul.odoo.model.request.CartToWishlistRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class BagItemsRecyclerHandler implements ChangeQtyDialogFragment.OnQtyChangeListener {

    @SuppressWarnings("unused")
    private static final String TAG = "BagItemsRecyclerHandler";

    private final Context context;
    private final BagItemData data;

    public BagItemsRecyclerHandler(Context context, BagItemData bagItemData) {
        this.context = context;
        data = bagItemData;
    }

    public void deleteItem() {
        AnalyticsImpl.INSTANCE.trackRemoveItemSelected(data.getLineId(), data.getName(), data.getPriceUnit());
        ((BaseActivity) context).mSweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getString(R.string.msg_are_you_sure))
                .setContentText(context.getString(R.string.ques_want_to_delete_this_product))
                .setConfirmText(context.getString(R.string.message_yes_delete_it))
                .setConfirmClickListener(sDialog -> {
                    // reuse previous dialog instance
                    sDialog.dismiss();
                    ApiConnection.deleteCartItem(context, AppSharedPref.getOrderId(context), data.getLineId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {

                        @Override
                        public void onNext(@NonNull BaseResponse baseResponse) {
                            super.onNext(baseResponse);
                            if (baseResponse.isAccessDenied()){
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(context);
                                        Intent i = new Intent(context, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)context).getClass().getSimpleName());
                                        context.startActivity(i);
                                    }
                                });
                            }else {
                                if (baseResponse.isSuccess()) {
                                    AnalyticsImpl.INSTANCE.trackRemoveItemSuccessful(data.getLineId(), data.getName(), data.getPriceUnit());
                                    ((BagActivity) context).getCartData();
                                    CustomToast.makeText(context, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                } else {
                                    AnalyticsImpl.INSTANCE.trackRemoveItemFailed(baseResponse.getMessage(), baseResponse.getResponseCode(), "");
                                    AlertDialogHelper.showDefaultWarningDialog(context, data.getName(), baseResponse.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                });
        ((BaseActivity) context).mSweetAlertDialog.show();
        ((BaseActivity) context).mSweetAlertDialog.showCancelButton(true);
    }

    public void viewProduct() {
        Intent intent = new Intent(context, ((OdooApplication) context.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, data.getProductId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, data.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, data.getName());
        context.startActivity(intent);
    }


    public void changeQty() {
        AnalyticsImpl.INSTANCE.trackItemQuantitySelected(data.getLineId(), data.getName(), data.getQty());
        ChangeQtyDialogFragment.newInstance(this, data.getQty()).show(((BaseActivity) context).mSupportFragmentManager, ChangeQtyDialogFragment.class.getSimpleName());
    }

    @Override
    public void onQtyChanged(int qty) {
        Toast.makeText(context, R.string.updating_bag, Toast.LENGTH_SHORT).show();
        hitUpdateCartApi(qty);
    }

    private Boolean isQuantityExceeding(int qty) {
        return qty > data.getAvailableQuantity();
    }

    private void showQuantityWarning(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setCustomImage(R.drawable.ic_warning)
            .setTitleText("")
            .setContentText(message)
            .setConfirmText(context.getString(R.string.continue_))
            .setConfirmClickListener(sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
            }).show();
    }

    public void hitUpdateCartApi(int qty) {
        ApiConnection.updateCart(context, AppSharedPref.getOrderId(context), data.getLineId(), qty).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(context) {

            /*not adding this subscription to Composite Disposable list as ChangeQtyDialogFragment dismiss occur after when this request is subscribed and thus cancelled on its dismissal.*/
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure),context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(context);
                            Intent i = new Intent(context, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)context).getClass().getSimpleName());
                            context.startActivity(i);
                        }
                    });
                }
                if (baseResponse.isSuccess()) {
                    AnalyticsImpl.INSTANCE.trackItemQuantityChangeSuccessful(data.getLineId(), data.getName(), qty);
                    ((BagActivity) context).getCartData();
                } else {
                    AnalyticsImpl.INSTANCE.trackItemQuantityChangeFailed(baseResponse.getMessage(), baseResponse.getResponseCode(), "");
                    showQuantityWarning(baseResponse.getMessage().replace(".0", ""));
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void addToWishlist() {
        AnalyticsImpl.INSTANCE.trackMoveToWishlistSelected(data.getLineId(), data.getName(), data.getPriceUnit());
        ApiConnection.cartToWishlist(context, new CartToWishlistRequest(data.getLineId())).subscribeOn(Schedulers
                .io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(context) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse response) {
                        super.onNext(response);
                        if (response.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(context);
                                    Intent i = new Intent(context, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)context).getClass().getSimpleName());
                                    context.startActivity(i);
                                }
                            });
                        } else {
                            if (response.isSuccess()) {
                                AnalyticsImpl.INSTANCE.trackMoveToWishlistSuccessful(data.getLineId(), data.getName(), data.getPriceUnit());
                                ((BagActivity) context).getCartData();
                                CustomToast.makeText(context, response.getMessage(), Toast.LENGTH_LONG, R.style.GenericStyleableToast).show();
                            } else {
                                AnalyticsImpl.INSTANCE.trackMoveToWishlistFailed(response.getMessage(), response.getResponseCode(), "");
                                AlertDialogHelper.showDefaultErrorDialog(context, context.getString(
                                        R.string.move_to_wishlist), response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
