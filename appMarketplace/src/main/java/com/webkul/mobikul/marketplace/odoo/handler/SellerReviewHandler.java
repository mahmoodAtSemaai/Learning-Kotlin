package com.webkul.mobikul.marketplace.odoo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivitySellerProfileBinding;
import com.webkul.mobikul.marketplace.odoo.fragment.AddSellerReviewFragment;
import com.webkul.mobikul.marketplace.odoo.model.SellerReviewForm;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerReviewRequest;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class SellerReviewHandler {
    ActivitySellerProfileBinding binding;
    private Context mContext;
    private int sellerId;

    public SellerReviewHandler(Context mContext, int sellerId) {
        this.mContext = mContext;
        this.sellerId = sellerId;
    }

    public void onClickMakeSellerReview() {
        if (AppSharedPref.isLoggedIn(mContext)) {
            FragmentHelper.addFragment(R.id.container, mContext, AddSellerReviewFragment.newInstance(sellerId), AddSellerReviewFragment.class.getSimpleName(), true, true);
        } else {
            SnackbarHelper.getSnackbar(((Activity) mContext), mContext.getString(R.string.please_login_to_write_reviews), Snackbar.LENGTH_LONG).show();
        }
    }

    public void addNewReview(SellerReviewForm mData) {
        Helper.hideKeyboard((AppCompatActivity) mContext);
        if (mData.isFormValidated()) {
            if (mData.getRating() == 0) {
                CustomToast.makeText(mContext, mContext.getString(R.string.please_add_your_rating), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                return;
            }
            AlertDialogHelper.showDefaultProgressDialog(mContext);
            MarketplaceApiConnection.postSellerReviewData(mContext, String.valueOf(sellerId), new SellerReviewRequest(mData.getRating()
                    , mData.getTitle(), mData.getMsg())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new CustomObserver<BaseResponse>(mContext) {
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
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((Activity)mContext).getClass().getSimpleName());
                                        mContext.startActivity(i);
                                    }
                                });
                            }else {
                                if (response.isSuccess()) {
                                    AlertDialogHelper.showDefaultSuccessDialog(mContext, mContext.getString(R.string.add_review), response.getMessage());
                                    FragmentHelper.popFromBackStack(mContext);
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(mContext, mContext.getString(R.string.add_review), response.getMessage());
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
