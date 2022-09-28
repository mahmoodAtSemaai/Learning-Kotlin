package com.webkul.mobikul.odoo.handler.home;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.core.utils.AppConstantsKt;
import com.webkul.mobikul.odoo.data.request.AddToWishListRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.data.entity.ProductEntity;
import com.webkul.mobikul.odoo.databinding.ItemCatalogProductListBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductGridBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest;
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2;
import com.webkul.mobikul.odoo.model.generic.AttributeData;
import com.webkul.mobikul.odoo.model.generic.ProductCombination;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.generic.ProductVariant;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ProductHandler {

    private Context context;
    private ProductEntity mData;
    private long mLastClickTime = 0;
    private ItemProductGridBinding mProductDefaultBinding;
    private ItemCatalogProductListBinding mProductListtBinding;
    private View imageView;
    private String TAG = "ProductHandler";


    public ProductHandler(Context context, ProductEntity productData) {
        this.context = context;
        mData = productData;
    }

    public void viewProduct() {
        AnalyticsImpl.INSTANCE.trackProductItemSelected(Helper.getScreenName(context),
                mData.getProductId(), mData.getName());
        Intent intent = new Intent(context, ProductActivityV2.class);
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getProductId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
//        Pair<View, String> p1 = Pair.create((View)mProductDefaultBinding.productImage, "product_image");

        String transitionName = context.getString(R.string.transition);
        if (mProductDefaultBinding != null) {
            imageView = mProductDefaultBinding.productImage;
        } else
            imageView = mProductListtBinding.ivProduct;
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) context, imageView, transitionName);
//        mContext.startActivity(intent, options.toBundle());
        context.startActivity(intent);
    }

    public void onClickWishlistIcon(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        String productId = mData.getProductId();

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string
                    .error_could_not_add_to_wishlist_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType
                    .TYPE_WARNING).show();
            return;
        }

        if (mData.getAddedToWishlist()) {
            removeFromWishList(productId, v);
        } else {
            addToWishList(productId, v);
        }
    }

    private void addToWishList(String productId, View v) {
        ApiConnection.addItemToWishListV1(context, new AddToWishListRequest(Integer.parseInt(productId)))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<WishListUpdatedResponse>(context) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull WishListUpdatedResponse response) {
                        super.onNext(response);
                        if (response.statusCode > AppConstantsKt.HTTP_RESPONSE_OK) {
                            redirectToSignInSignupActivity();
                        } else {
                            setWishListIcon(response, true, context, productId, mData, v);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void removeFromWishList(String productId, View v) {
        ApiConnection.removeItemFromWishListV1(context, new DeleteFromWishListRequest(Integer.parseInt(productId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<WishListUpdatedResponse>(context) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull WishListUpdatedResponse response) {
                        super.onNext(response);
                        if (response.statusCode > AppConstantsKt.HTTP_RESPONSE_OK) {
                            redirectToSignInSignupActivity();
                        } else {
                            setWishListIcon(response, false, context, productId, mData, v);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void redirectToSignInSignupActivity() {
        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                AppSharedPref.clearCustomerData(context);
                Intent i = new Intent(context, SignInSignUpActivity.class);
                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity) context).getClass().getSimpleName());
                context.startActivity(i);
            }
        });
    }

    private void setWishListIcon(WishListUpdatedResponse response, boolean isAddedToWishList, Context mContext, String productId, ProductEntity name, View v) {
        if (response.statusCode == AppConstantsKt.HTTP_RESPONSE_OK) {
            FirebaseAnalyticsImpl.logAddToWishlistEvent(mContext, productId, mData.getName());
        }
        mData.setAddedToWishlist(isAddedToWishList);
        ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                isAddedToWishList ? R.drawable.ic_vector_wishlist_red_24dp : R.drawable.ic_vector_wishlist_grey_24dp, null));
    }

    public void setProductDefaultBinding(ItemProductGridBinding mBinding) {
        mProductDefaultBinding = mBinding;
    }

    public void setProductListBinding(ItemCatalogProductListBinding mBinding) {
        mProductListtBinding = mBinding;
    }
}