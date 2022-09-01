package com.webkul.mobikul.odoo.handler.home;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HOME_PAGE_RESPONSE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_COLOR;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_HIDDEN;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_RADIO;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_SELECT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.core.utils.AppConstantsKt;
import com.webkul.mobikul.odoo.data.request.AddToWishListRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.databinding.ItemCatalogProductListBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductGridBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
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
    private ProductData mData;
    private long mLastClickTime = 0;
    private ItemProductGridBinding mProductDefaultBinding;
    private ItemCatalogProductListBinding mProductListtBinding;
    private View imageView;
    private String TAG = "ProductHandler";


    public ProductHandler(Context context, ProductData productData) {
        this.context = context;
        mData = productData;
    }

    public void viewProduct() {
        AnalyticsImpl.INSTANCE.trackProductItemSelected(Helper.getScreenName(context),
                mData.getProductId(), mData.getName());
        Intent intent = new Intent(context, ((OdooApplication) context.getApplicationContext()).getProductActivity());
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getProductId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, mData.getTemplateId());
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getName());
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
        HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
        if (homePageResponse != null) {
            intent.putExtra(BUNDLE_KEY_HOME_PAGE_RESPONSE, homePageResponse);
        }
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
        String productId;
        if (mData.getVariants().isEmpty()) {
            productId = mData.getProductId();
        } else {
            productId = getProductIdForVariant();
        }

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string
                    .error_could_not_add_to_wishlist_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType
                    .TYPE_WARNING).show();
            return;
        }

        if (mData.isAddedToWishlist()) {
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

    private void setWishListIcon(WishListUpdatedResponse response, boolean isAddedToWishList, Context mContext, String productId, ProductData name, View v) {
        if (response.statusCode == AppConstantsKt.HTTP_RESPONSE_OK) {
            FirebaseAnalyticsImpl.logAddToWishlistEvent(mContext, productId, mData.getName());
        }
        mData.setAddedToWishlist(isAddedToWishList);
        ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(),
                isAddedToWishList ? R.drawable.ic_vector_wishlist_red_24dp : R.drawable.ic_vector_wishlist_grey_24dp, null));
    }

    private String getProductIdForVariant() {
        Map<String, String> attrValueIdMap = new HashMap<>();
        for (AttributeData eachAttributeData : mData.getAttributes()) {
            switch (eachAttributeData.getType()) {
                case ATTR_TYPE_COLOR:
                    RadioGroup colorTypeRg = ((ProductActivity) context).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), colorTypeRg.findViewById(colorTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    break;

                case ATTR_TYPE_RADIO:
                    RadioGroup radioTypeRg = ((ProductActivity) context).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), radioTypeRg.findViewById(radioTypeRg.getCheckedRadioButtonId()).getTag().toString());
                    break;

                case ATTR_TYPE_SELECT:
                case ATTR_TYPE_HIDDEN:
                    AppCompatSpinner selectTypeSpinner = ((ProductActivity) context).mBinding.attributesContainer.findViewWithTag(eachAttributeData.getType() + eachAttributeData.getAttributeId());
                    attrValueIdMap.put(eachAttributeData.getAttributeId(), eachAttributeData.getAttributeOptionDatas().get(selectTypeSpinner.getSelectedItemPosition()).getValueId());
                    break;
            }
        }


        for (ProductVariant eachProductVariant : mData.getVariants()) {
            Map<String, String> tempAttrValueIdMap = new HashMap<>();
            for (ProductCombination eachProductCombination : eachProductVariant.getCombinations()) {
                tempAttrValueIdMap.put(eachProductCombination.getAttributeId(), eachProductCombination.getValueId());
            }
            if (tempAttrValueIdMap.equals(attrValueIdMap)) {
                return eachProductVariant.getProductId();
            }
        }
        return null;
    }

    public void setProductDefaultBinding(ItemProductGridBinding mBinding) {
        mProductDefaultBinding = mBinding;
    }

    public void setProductListBinding(ItemCatalogProductListBinding mBinding) {
        mProductListtBinding = mBinding;
    }
}