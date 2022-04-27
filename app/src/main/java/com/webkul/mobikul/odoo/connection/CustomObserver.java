package com.webkul.mobikul.odoo.connection;

import android.app.Activity;
import android.content.Context;

import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public abstract class CustomObserver<T> implements Observer<T> {
    @SuppressWarnings("unused")
    private static final String TAG = "CustomObserver";
    private Context mContext;

    protected CustomObserver(@androidx.annotation.NonNull Context context) {
        mContext = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).mCompositeDisposable.add(d);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        AlertDialogHelper.dismiss(mContext);
        if (t instanceof BaseResponse && mContext instanceof BaseActivity) {
            if (!(t instanceof StateListResponse)){
                ((BaseActivity) mContext).updateCartBadge(((BaseResponse) t).getCartCount());
                ((BaseActivity) mContext).updateEmailVerification(((BaseResponse) t).isEmailVerified());
                AppSharedPref.setAllowedReview(mContext, ((BaseResponse) t).isAllowReviewModule());
                AppSharedPref.setGdprEnable(mContext, ((BaseResponse) t).isGdprEnable());
                AppSharedPref.setAllowedWishlist(mContext, ((BaseResponse) t).isAllowWishlistModule());
                AppSharedPref.setItemsPerPage(mContext, ((BaseResponse) t).getItemsPerPage());
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable t) {

        NetworkHelper.onFailure(t, (Activity) mContext);
    }

    @Override
    public void onComplete() {

    }
}
