package com.webkul.mobikul.odoo.handler.bag;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BagActivity;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
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

    private final Context mContext;
    private final BagItemData mData;

    public BagItemsRecyclerHandler(Context context, BagItemData bagItemData) {
        mContext = context;
        mData = bagItemData;
    }

    public void deleteItem() {
        ((BaseActivity) mContext).mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getString(R.string.msg_are_you_sure))
                .setContentText(mContext.getString(R.string.ques_want_to_delete_this_product))
                .setConfirmText(mContext.getString(R.string.message_yes_delete_it))
                .setConfirmClickListener(sDialog -> {
                    // reuse previous dialog instance
                    sDialog.dismiss();
                    ApiConnection.deleteCartItem(mContext, mData.getLineId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {

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
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            }else {
                                if (baseResponse.isSuccess()) {
                                    ((BagActivity) mContext).getCartData();
                                    CustomToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                } else {
                                    AlertDialogHelper.showDefaultWarningDialog(mContext, mData.getName(), baseResponse.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                });
        ((BaseActivity) mContext).mSweetAlertDialog.show();
        ((BaseActivity) mContext).mSweetAlertDialog.showCancelButton(true);
    }

    public void viewProduct() {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        mContext.startActivity(intent);
    }


    public void changeQty() {
        ChangeQtyDialogFragment.newInstance(this, mData.getQty()).show(((BaseActivity) mContext).mSupportFragmentManager, ChangeQtyDialogFragment.class.getSimpleName());
    }

    @Override
    public void onQtyChanged(int qty) {
        Toast.makeText(mContext, R.string.updating_bag, Toast.LENGTH_SHORT).show();
        ApiConnection.updateCart(mContext, mData.getLineId(), qty).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(mContext) {

            /*not adding this subscription to Composite Disposable list as ChangeQtyDialogFragment dismiss occur after when this request is subscribed and thus cancelled on its dismissal.*/
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull BaseResponse baseResponse) {
                super.onNext(baseResponse);
                if (baseResponse.isAccessDenied()){
                    AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure),mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            AppSharedPref.clearCustomerData(mContext);
                            Intent i = new Intent(mContext, SignInSignUpActivity.class);
                            i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)mContext).getClass().getSimpleName());
                            mContext.startActivity(i);
                        }
                    });
                }
                if (baseResponse.isSuccess()) {
                    ((BagActivity) mContext).getCartData();
                } else {
                    AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(R.string.error_something_went_wrong), baseResponse.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void addToWishlist() {
        ApiConnection.cartToWishlist(mContext, new CartToWishlistRequest(mData.getLineId())).subscribeOn(Schedulers
                .io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(mContext) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(mContext);
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse response) {
                        super.onNext(response);
                        if (response.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(mContext);
                                    Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BagActivity)mContext).getClass().getSimpleName());
                                    mContext.startActivity(i);
                                }
                            });
                        }else {
                            if (response.isSuccess()) {
                                ((BagActivity) mContext).getCartData();
                                CustomToast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG, R.style.GenericStyleableToast).show();
                            } else {
                                AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(
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
