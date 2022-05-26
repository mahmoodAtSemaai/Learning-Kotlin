package com.webkul.mobikul.odoo.adapter.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.analytics.AnalyticsSourceConstants;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentSignUpBinding;
import com.webkul.mobikul.odoo.fragment.LoginFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.ErrorConstants;
import com.webkul.mobikul.odoo.helper.FingerPrintLoginHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse;
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse;
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpData;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;
import com.webkul.mobikul.odoo.model.request.SignUpRequest;
import com.webkul.mobikul.odoo.model.user.UserModel;
import com.webkul.mobikul.odoo.updates.FirebaseRemoteConfigHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class SignUpHandler {
    @SuppressWarnings("unused")
    protected static final String TAG = "SignUpHandler";
    protected final Context context;
    protected final SignUpData data;
    protected FragmentSignUpBinding mBinding;
    protected boolean isSeller = false;
    protected boolean isTermAndCondition = false;
    protected boolean isMarketplaceTermAndCondition = false;
    protected String countryId;


    public SignUpHandler(Context context, SignUpData data, FragmentSignUpBinding mBinding) {
        this.context = context;
        this.data = data;
        this.mBinding = mBinding;
    }

    public void signUp() {

        Helper.hideKeyboard((AppCompatActivity) context);
        if (data.isFormValidated()) {
            if (AppSharedPref.isTermAndCondition(context)) {
                if (isTermAndCondition) {

                    if (BuildConfig.isMarketplace && isSeller) {
                        if (isMarketplaceTermAndCondition) {
                            handleSignUp(data);
                        } else {
                            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        handleSignUp(data);
                    }
                } else {
                    SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string.plese_accept_tnc), Snackbar.LENGTH_LONG).show();
                }
            } else {
                handleSignUp(data);
            }
        } else {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string.error_enter_valid_login_details), Snackbar.LENGTH_LONG).show();
        }
    }

    public void handleSignUp(SignUpData data) {
        AlertDialogHelper.showDefaultProgressDialog(context);
        AppSharedPref.setCustomerLoginBase64Str(context, Base64.encodeToString(new AuthenticationRequest(this.data.getPhoneNumber(), this.data.getPassword()).toString().getBytes(), Base64.NO_WRAP));

        AnalyticsImpl.INSTANCE.trackSignupSelected(AnalyticsSourceConstants.EVENT_SOURCE_SIGNUP,
                AnalyticsSourceConstants.EVENT_SOURCE_PROPERTY_MOBILE, data.getName(), Helper.getStringDateAndTime(), isSeller, countryId);
        callSignUpApi(context, new SignUpRequest(context, this.data.getName(), this.data.getPhoneNumber(), this.data.getPassword(), this.data.getReferralCode(), false, isSeller, this.data.getProfileURL(), countryId));
    }

    private void callSignUpApi(Context context, SignUpRequest signUpRequest) {
        ApiConnection.signUp(context, signUpRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(getSignUpResponseObserver(context));

    }

    private Observer<? super SignUpResponse> getSignUpResponseObserver(Context context) {
        return new CustomObserver<SignUpResponse>(context) {
            @Override
            public void onNext(@NonNull SignUpResponse signUpResponse) {
                super.onNext(signUpResponse);
                if (signUpResponse.isSuccess()) {
                    AnalyticsImpl.INSTANCE.trackSignupSuccessfull(AnalyticsSourceConstants.EVENT_SOURCE_SIGNUP,
                            AnalyticsSourceConstants.EVENT_SOURCE_PROPERTY_MOBILE,
                            data.getName(),
                            Helper.getStringDateAndTime(),
                            isSeller,
                            countryId);

                    if(isSeller && FirebaseRemoteConfigHelper.isChatFeatureEnabled()) {
                        ApiConnection.createChannel(context).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).
                                subscribe(new CustomObserver<ChatBaseResponse<ChatCreateChannelResponse>>(context) {
                                });
                    }

                    fetchBillingAddress(context, signUpResponse);
                } else {
                    AnalyticsImpl.INSTANCE.trackSignupFailed(
                            (long) signUpResponse.getResponseCode(), ErrorConstants.SignupError.INSTANCE.getErrorType(), signUpResponse.getMessage()
                    );
                    AppSharedPref.setCustomerLoginBase64Str(context, "");
                    SnackbarHelper.getSnackbar((Activity) context, signUpResponse.getMessage(), Snackbar.LENGTH_LONG, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@androidx.annotation.NonNull Throwable t) {
                super.onError(t);
                AnalyticsImpl.INSTANCE.trackSignupFailed(
                        (long) ErrorConstants.SignupError.INSTANCE.getErrorCode(),
                        ErrorConstants.SignupError.INSTANCE.getErrorType(),
                        ErrorConstants.SignupError.INSTANCE.getErrorMessage()
                );
            }
        };
    }

    private void fetchBillingAddress(Context context, SignUpResponse signUpResponse) {
        ApiConnection.getAddressBookData(context, new BaseLazyRequest(0, 1)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(getAddressResponseObserver(context, signUpResponse));
    }

    private Observer<? super MyAddressesResponse> getAddressResponseObserver(Context context, SignUpResponse signUpResponse) {
        return new CustomObserver<MyAddressesResponse>(context) {
            @Override
            public void onNext(@NonNull MyAddressesResponse myAddressesResponse) {
                super.onNext(myAddressesResponse);
                String billingAddressUrl = myAddressesResponse.getAddresses().get(0).getUrl();
                showAlertDialogForFingerPrintVerification(context, signUpResponse, billingAddressUrl);
            }
        };
    }

    private void showAlertDialogForFingerPrintVerification(Context context, SignUpResponse signUpResponse, String billingAddressUrl) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getString(R.string.account_created_successfully))
                .setContentText(signUpResponse.getMessage())
                .setConfirmText(context.getString(R.string.continue_shopping))
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    FingerPrintLoginHelper fingerPrintLoginHelper = new FingerPrintLoginHelper();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        fingerPrintLoginHelper.askForFingerprintLogin(context, null, null, signUpResponse, SignUpHandler.this.data, billingAddressUrl);
                    } else {
                        AppSharedPref.setIsAllowedFingerprintLogin(context, false);
                        fingerPrintLoginHelper.navigateToHomeAfterAnalyticsSetup(context, null, null, signUpResponse, SignUpHandler.this.data, billingAddressUrl);
                    }
                }).show();
    }

    public void viewTermNCond() {
        ApiConnection.getTermAndCondition(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<TermAndConditionResponse>(context) {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                super.onSubscribe(d);
                AlertDialogHelper.showDefaultProgressDialog(context);
            }

            @Override
            public void onNext(@NonNull TermAndConditionResponse termAndConditionResponse) {
                super.onNext(termAndConditionResponse);
                if (termAndConditionResponse.isSuccess()) {
                    LinearLayout addedLayout = new LinearLayout(context);
                    addedLayout.setOrientation(LinearLayout.VERTICAL);
                    WebView myWebView = new WebView(context);

                    // enable webview
                    Helper.enableDarkModeInWebView(context, myWebView);

                    String mime = "text/html";
                    String encoding = "utf-8";
                    myWebView.loadDataWithBaseURL("", termAndConditionResponse.getTermsAndConditions(), mime, encoding, "");
                    addedLayout.addView(myWebView);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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

    public void signIn() {
        Helper.hideKeyboard((AppCompatActivity) context);
        FragmentHelper.replaceFragment(android.R.id.content, context, LoginFragment.newInstance(), LoginFragment.class.getSimpleName(), false, false);
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
        if (seller) {
            mBinding.countryLayout.setVisibility(View.VISIBLE);
            mBinding.urlLayout.setVisibility(View.VISIBLE);
            if (AppSharedPref.isTermAndCondition(context)) {
                mBinding.marketplaceTncLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.marketplaceTncLayout.setVisibility(View.GONE);
            }

        } else {
            mBinding.countryLayout.setVisibility(View.GONE);
            mBinding.urlLayout.setVisibility(View.GONE);
            mBinding.marketplaceTncLayout.setVisibility(View.GONE);
        }
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public boolean isTermAndCondition() {
        return isTermAndCondition;
    }

    public void setTermAndCondition(boolean termAndCondition) {
        isTermAndCondition = termAndCondition;
    }

    public void viewMarketplaceTermNCond() {

    }


    public boolean isMarketplaceTermAndCondition() {
        return isMarketplaceTermAndCondition;
    }

    public void setMarketplaceTermAndCondition(boolean marketplaceTermAndCondition) {
        isMarketplaceTermAndCondition = marketplaceTermAndCondition;
    }
}
