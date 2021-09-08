package com.webkul.mobikul.odoo.handler.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.fragment.AddProductReviewFragment;
import com.webkul.mobikul.odoo.fragment.ProductReviewFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.product.Review;
import com.webkul.mobikul.odoo.model.request.AddProductReviewRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.activity.HomeActivity.RC_SIGN_IN_SIGN_UP;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 25/7/17.
 */

public class ProductReviewHandler {

    private static final String TAG = "ProductReviewHandler";
    private final Context mContext;
    private String templateId;
    private long mLastClickTime = 0;

    public ProductReviewHandler(Context context, String templateID) {
        mContext = context;
        templateId = templateID;
    }

    public void onClickAddReview() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if (AppSharedPref.isLoggedIn(mContext)) {
            FragmentHelper.addFragment(R.id.container, mContext, AddProductReviewFragment.newInstance(templateId), AddProductReviewFragment.class.getSimpleName(), true, true);
        } else {
            SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText(mContext.getString(R.string.error))
                    .setContentText(mContext.getString(R.string.error_please_login_to_continue))
                    .setConfirmClickListener(sweetAlertDialog -> {
                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, HomeActivity.class.getSimpleName());
                        ((ProductActivity) mContext).startActivityForResult(i, RC_SIGN_IN_SIGN_UP);
                        sweetAlertDialog.dismiss();
                    });
            dialog.setCancelable(true);
            dialog.show();
        }

    }

    public void addNewReview(Review mData) {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (mData.isFormValidated()) {
            if (mData.getRating() == 0) {
                CustomToast.makeText(mContext, mContext.getString(R.string.please_add_your_rating), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                return;
            }
            AlertDialogHelper.showDefaultProgressDialog(mContext);
            ApiConnection.addNewProductReview(mContext, new AddProductReviewRequest(mContext, mData.getRating()
                    , mData.getTitle(), mData.getMsg(), templateId)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new CustomObserver<BaseResponse>(mContext) {
                        @Override
                        public void onNext(@NonNull BaseResponse response) {
                            super.onNext(response);
                            if (response.isAccessDenied()) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(mContext, mContext.getString(R.string.error_login_failure), mContext.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(mContext);
                                        Intent i = new Intent(mContext, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivity) mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            } else {
                                if (response.isSuccess()) {
                                    AlertDialogHelper.showDefaultSuccessOneLinerDialog(mContext, response.getMessage());
                                    FragmentHelper.popFromBackStack(mContext);
                                    if ((((ProductActivity) mContext).getSupportFragmentManager().findFragmentByTag(ProductReviewFragment
                                            .class.getSimpleName())) != null) {
                                        (((ProductActivity) mContext).getSupportFragmentManager().findFragmentByTag(ProductReviewFragment
                                                .class.getSimpleName())).onResume();
                                    } else {
                                        ((ProductActivity) mContext).onResume();
                                    }
                                } else {
                                    AlertDialogHelper.showDefaultErrorOneLinerDialog(mContext, response.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            SnackbarHelper.getSnackbar((Activity) mContext, mContext.getString(R.string.please_enter_required_details), Snackbar.LENGTH_LONG).show();
        }

    }
}
