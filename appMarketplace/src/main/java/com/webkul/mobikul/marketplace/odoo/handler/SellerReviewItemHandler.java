package com.webkul.mobikul.marketplace.odoo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProductsListActivity;
import com.webkul.mobikul.marketplace.odoo.adapter.SellerReviewAdapter;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.model.SellerReview;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.ReviewLikeDislikeResponse;
import com.webkul.mobikul.odoo.model.request.ReviewLikeDislikeRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
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
public class SellerReviewItemHandler {
    private Context mContext;
    private SellerReviewAdapter mAdapter;
    private int mPosition;


    public SellerReviewItemHandler(Context mContext, SellerReviewAdapter adapter, int position) {
        this.mContext = mContext;
        this.mAdapter = adapter;
        this.mPosition = position;
    }

    public void onClickLikeDislike(boolean isHelpful, SellerReview data) {
        AlertDialogHelper.showDefaultProgressDialog(mContext);
        MarketplaceApiConnection.reviewLikeDislike(mContext, new ReviewLikeDislikeRequest(data.getId(), isHelpful)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ReviewLikeDislikeResponse>(mContext) {
                    @Override
                    public void onNext(@NonNull ReviewLikeDislikeResponse response) {
                        super.onNext(response);
                        AlertDialogHelper.dismiss(mContext);
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
                                CustomToast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT, com.webkul.mobikul.odoo.R.style.GenericStyleableToast).show();
                                if (mAdapter != null) {
                                    mAdapter.updateListItem(response, mPosition);
                                }
                                /*if (mAdapter != null){
                                    if (isHelpful){
                                        int currentCount =  Integer.parseInt(data.getHelpful());
                                        data.setHelpful(currentCount+1+"");
                                    }else {
                                        int currentCount =  Integer.parseInt(data.getNotHelpful());
                                        data.setNotHelpful(currentCount+1+"");
                                    }
                                    mAdapter.notifyItemChanged(mPosition,data);
                                }*/

                            } else {
                                AlertDialogHelper.showDefaultErrorOneLinerDialog(mContext, response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
