package com.webkul.mobikul.marketplace.odoo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProductsListActivity;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivityBecomeSellerBinding;
import com.webkul.mobikul.marketplace.odoo.model.BecomeSellerData;
import com.webkul.mobikul.marketplace.odoo.model.request.BecomeSellerRequest;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Webkul Software.
 * <p>
 * Android Studio version 3.0+
 * Java version 1.7+
 *
 * @author Webkul <support@webkul.com>
 * @category Webkul
 * @package com.webkul.mobikul.marketplace.odoo.handler
 * @copyright 2010-2018 Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class BecomeSellerActivityHandler {
    private Context mContext;
    private ActivityBecomeSellerBinding mBinding;

    public BecomeSellerActivityHandler(Context mContext, ActivityBecomeSellerBinding mBinding) {
        this.mContext = mContext;
        this.mBinding = mBinding;
    }

    public void viewTermNCond() {

        ApiConnection.getTermAndCondition(mContext).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<TermAndConditionResponse>(mContext) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(mContext);
            }

            @Override
            public void onNext(@NonNull TermAndConditionResponse termAndConditionResponse) {
                super.onNext(termAndConditionResponse);
                if (termAndConditionResponse.isSuccess()) {
                    LinearLayout addedLayout = new LinearLayout(mContext);
                    addedLayout.setOrientation(LinearLayout.VERTICAL);
                    WebView myWebView = new WebView(mContext);

                    Helper.enableDarkModeInWebView(mContext, myWebView);

                    String mime = "text/html";
                    String encoding = "utf-8";
                    myWebView.loadDataWithBaseURL("", termAndConditionResponse.getTermsAndConditions(), mime, encoding, "");
                    addedLayout.addView(myWebView);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setView(addedLayout);
                    dialog.show();
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

    public void becomeSeller(BecomeSellerData data){
        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (data.isFormValidated(mBinding)){
            AlertDialogHelper.showDefaultProgressDialog(mContext);

            MarketplaceApiConnection.becomeSeller(mContext, new BecomeSellerRequest(data.getProfileURL(),data.getCountryId()))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CustomObserver<BaseResponse>(mContext){
                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                        }

                        @Override
                        public void onNext(BaseResponse baseResponse) {
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

                                CustomToast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                if (baseResponse.isSuccess()) {
                                    ((Activity) mContext).finish();
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                        }
                    });

        }


    }
}
