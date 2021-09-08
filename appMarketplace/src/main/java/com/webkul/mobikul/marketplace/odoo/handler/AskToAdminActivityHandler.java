package com.webkul.mobikul.marketplace.odoo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.AskToAdminActivity;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProductsListActivity;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivityAskToAdminBinding;
import com.webkul.mobikul.marketplace.odoo.model.request.AskToAdminRequest;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.BaseResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 9/4/18 in Mobikul_Odoo_Application.
 */

public class AskToAdminActivityHandler {

    private Context mContext;
    private ActivityAskToAdminBinding mBinding;

    public AskToAdminActivityHandler(Context mContext, ActivityAskToAdminBinding mBinding) {
        this.mContext = mContext;
        this.mBinding = mBinding;
    }

    public void onClickSubmitRequest() {
        if (isValidate()) {
            MarketplaceApiConnection.getAskAdmin(mContext, new AskToAdminRequest(
                    mBinding.titleEt.getText().toString().trim(),
                    mBinding.descriptionEt.getText().toString().trim()))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<BaseResponse>(mContext) {
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
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity)mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            }else {
                                if (baseResponse.isSuccess()) {
                                    CustomToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, com.webkul.mobikul.odoo.R.style.GenericStyleableToast).show();
                                    ((AskToAdminActivity) mContext).finish();


                                } else {
                                    AlertDialogHelper.showDefaultErrorOneLinerDialog(mContext, baseResponse.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private boolean isValidate() {
        if (mBinding.titleEt.getText().toString().trim().isEmpty()) {
            mBinding.titleEt.setError(String.format("%s %s", mContext.getString(R.string.title),
                    mContext.getResources().getString(R.string.error_is_required)));
            return false;
        }
        if (mBinding.descriptionEt.getText().toString().trim().isEmpty()) {
            mBinding.descriptionEt.setError(String.format("%s %s", mContext.getString(R.string.description),
                    mContext.getResources().getString(R.string.error_is_required)));
            return false;
        }
        return true;
    }
}
