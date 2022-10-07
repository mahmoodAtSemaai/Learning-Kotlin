package com.webkul.mobikul.odoo.handler.product;

import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_ADD_TO_CART;
import static com.webkul.mobikul.odoo.activity.ProductActivity.RC_BUY_NOW;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHAT_TITLE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_REQ_CODE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_COLOR;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_HIDDEN;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_RADIO;
import static com.webkul.mobikul.odoo.helper.ProductHelper.ATTR_TYPE_SELECT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.ChatActivity;
import com.webkul.mobikul.odoo.activity.ProductActivity;
import com.webkul.mobikul.odoo.activity.ProductActivityV1;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.core.utils.AppConstantsKt;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.data.request.AddToWishListRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.dialog_frag.ChangeQtyDialogFragment;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.fragment.ProductReviewFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;
import com.webkul.mobikul.odoo.model.generic.AttributeData;
import com.webkul.mobikul.odoo.model.generic.ProductCombination;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.generic.ProductVariant;
import com.webkul.mobikul.odoo.model.product.AddToCartResponse;
import com.webkul.mobikul.odoo.model.request.AddToBagRequest;
import com.webkul.mobikul.odoo.data.request.AddToWishListRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.ui.seller.SellerProfileActivityV1;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shubham.agarwal on 23/5/17.
 */

public class ProductActivityHandler implements ChangeQtyDialogFragment.OnQtyChangeListener {
    @SuppressWarnings("unused")
    private static final String TAG = "ProductActivityHandler";
    private final Context context;
    private final ProductData data;
    private long mLastClickTime = 0;
    private static final int QTY_ZERO = 0;
    private static final int QTY_MAX_ALLOWED = 9999999;


    public ProductActivityHandler(Context context, ProductData productData) {
        this.context = context;
        data = productData;
    }

    public void changeQty() {
        ChangeQtyDialogFragment.newInstance(this, data.getQuantity()).show(((BaseActivity) context).mSupportFragmentManager, ChangeQtyDialogFragment.class.getSimpleName());
    }

    public void setWishlistForvariants(boolean isAddedToWishlist) {
        data.setAddedToWishlist(isAddedToWishlist);
    }

    @Override
    public void onQtyChanged(int qty) {
        qty = isQuantityExceeding(qty);
        data.setQuantity(qty);
    }

    private int isQuantityExceeding(int qty) {
        if (data.isOutOfStock() && !data.isService()) {
            ((ProductActivityV1) context).showWarning(true);
            return data.getAvailableQuantity();
        } else if (!data.isNever() && !data.isPreOrder() && !data.isCustom() && !data.isService() && qty > data.getAvailableQuantity()) {
            ((ProductActivityV1) context).showWarning(false);
            return data.getAvailableQuantity();
        }
        return qty;
    }

    private void showQuantityWarning(String message) {
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.ic_warning)
                .setTitleText("")
                .setContentText(message)
                .setConfirmText(context.getString(R.string.continue_))
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                }).show();
    }

    public void addToCart(boolean isBuyNow) {
        if (!AppSharedPref.isLoggedIn(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            builder.setTitle(R.string.guest_user)
                    .setMessage(R.string.error_please_login_to_continue)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        Intent intent = new Intent(context, SignInSignUpActivity.class);
                        intent.putExtra(BUNDLE_KEY_REQ_CODE, isBuyNow ? RC_BUY_NOW : RC_ADD_TO_CART);
                        intent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ProductActivityV1.class.getSimpleName());
                        ((ProductActivityV1) context).startActivityForResult(intent, isBuyNow ? RC_BUY_NOW : RC_ADD_TO_CART);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return;
        }

        String productId;
        if (data.getVariants().isEmpty()) {
            productId = data.getProductId();
        } else {
            productId = getProductIdForVariant();

        }

        if (checkQuantity(data.getQuantity())) {
            ((ProductActivityV1) context).showWarning(true);
            return;
        }

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string.error_could_not_add_to_bag_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }

        if (!data.isNever() && !data.isPreOrder() && !data.isCustom() && !data.isService() && data.getQuantity() > data.getAvailableQuantity()) {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string.product_not_available_in_this_quantity), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType.TYPE_WARNING).show();
            return;
        }

        ApiConnection.addToCart(context, new AddToBagRequest(productId, data.getQuantity())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<AddToCartResponse>(context) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull AddToCartResponse addToCartResponse) {
                        super.onNext(addToCartResponse);
                        if (addToCartResponse.isAccessDenied()) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(context);
                                    Intent i = new Intent(context, SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivityV1) context).getClass().getSimpleName());
                                    context.startActivity(i);
                                }
                            });
                        } else {
                            FirebaseAnalyticsImpl.logAddToCartEvent(context, productId, addToCartResponse.getProductName());
                            if (addToCartResponse.isSuccess()) {
                                AnalyticsImpl.INSTANCE.trackAddItemToBagSuccessful(data.getQuantity(),
                                        data.getProductId(), data.getSeller().getMarketplaceSellerId(),
                                        data.getName());
                            } else {
                                AnalyticsImpl.INSTANCE.trackAddItemToBagFailed(addToCartResponse.getMessage(),
                                        addToCartResponse.getResponseCode(), "");
                            }
                            if (isBuyNow) {
                                if (addToCartResponse.isSuccess()) {
                                    IntentHelper.beginCheckout(context);
                                } else {
                                    showQuantityWarning(addToCartResponse.getMessage());
                                }
                            } else {
                                if (addToCartResponse.isSuccess()) {
                                    ((ProductActivityV1) context).showSuccessfullDialog();
                                    ((ProductActivityV1) context).getBagItemsCount();
                                } else {
                                    ((ProductActivityV1) context).showWarning(false);
                                    ((ProductActivityV1) context).getBagItemsCount();
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getProductIdForVariant() {
        Map<String, String> attrValueIdMap = new HashMap<>();
        for (AttributeData eachAttributeData : data.getAttributes()) {
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


        for (ProductVariant eachProductVariant : data.getVariants()) {
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

    public void shareProduct() {
        AnalyticsImpl.INSTANCE.trackShareButtonSelected(data.getProductId(), data.getName());
        Intent sendIntent = new Intent();
        FirebaseAnalyticsImpl.logShareEvent(context, data.getProductId(), data.getName());
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data.getAbsoluteUrl());
        sendIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.startActivity(Intent.createChooser(sendIntent, "Choose an Action:", null));
        } else {
            context.startActivity(sendIntent);
        }
    }

    public void onClickWishlistIcon(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        String productId;
        if (data.getVariants().isEmpty()) {
            productId = data.getProductId();
        } else {
            productId = getProductIdForVariant();
        }

        if (productId == null) {
            SnackbarHelper.getSnackbar((Activity) context, context.getString(R.string
                    .error_could_not_add_to_wishlist_pls_try_again), Snackbar.LENGTH_SHORT, SnackbarHelper.SnackbarType
                    .TYPE_WARNING).show();
            return;
        }

        if (data.isAddedToWishlist()) {
            ApiConnection.removeItemFromWishListV1(context, new DeleteFromWishListRequest(Integer.parseInt(productId)))
                    .subscribeOn(Schedulers.io())
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
                            if (response.statusCode == AppConstantsKt.HTTP_ERROR_UNAUTHORIZED_REQUEST) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(context);
                                        Intent i = new Intent(context, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivityV1) context).getClass().getSimpleName());
                                        context.startActivity(i);
                                    }
                                });
                            } else {
                                if (response.statusCode == AppConstantsKt.HTTP_RESPONSE_OK ||
                                        response.statusCode == AppConstantsKt.HTTP_RESPONSE_RESOURCE_CREATED) {
                                    data.setAddedToWishlist(false);
                                    CustomToast.makeText(context, response.message, Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                            R.drawable.ic_vector_wishlist_grey_24dp, null));
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(context, context.getString(
                                            R.string.move_to_wishlist), response.message);
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else {
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
                            if (response.statusCode == AppConstantsKt.HTTP_ERROR_UNAUTHORIZED_REQUEST) {
                                AlertDialogHelper.showDefaultWarningDialogWithDismissListener(context, context.getString(R.string.error_login_failure), context.getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        AppSharedPref.clearCustomerData(context);
                                        Intent i = new Intent(context, SignInSignUpActivity.class);
                                        i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((ProductActivityV1) context).getClass().getSimpleName());
                                        context.startActivity(i);
                                    }
                                });


                            } else {
                                if (response.statusCode == AppConstantsKt.HTTP_RESPONSE_OK ||
                                        response.statusCode == AppConstantsKt.HTTP_RESPONSE_RESOURCE_CREATED) {
                                    data.setAddedToWishlist(true);
                                    FirebaseAnalyticsImpl.logAddToWishlistEvent(context, productId, data.getName());
                                    CustomToast.makeText(context, response.message, Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                                    ((ImageButton) v).setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                            R.drawable.ic_vector_wishlist_red_24dp, null));
                                } else {
                                    AlertDialogHelper.showDefaultErrorDialog(context, context.getString(
                                            R.string.move_to_wishlist), response.message);
                                }
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void getAllReviews() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        FragmentHelper.replaceFragment(R.id.container, context, ProductReviewFragment.newInstance(data.getTemplateId()), ProductReviewFragment.class.getSimpleName(), true, true);
    }

    public void onClickSellerName(String sellerID) {
        AnalyticsImpl.INSTANCE.trackSellerProfileSelected(Helper.getScreenName(context),
                data.getSeller().getMarketplaceSellerId(), (int) data.getSeller().getAverageRating(),
                data.getSeller().getSellerName());
        Intent intent = new Intent(context, SellerProfileActivityV1.class);
        intent.putExtra(BUNDLE_KEY_SELLER_ID, sellerID);
        context.startActivity(intent);
    }

    public void onClickChatButton(String sellerId, String sellerName, String sellerProfileImage) {
        launchChatActivity(Integer.parseInt(sellerId), sellerName, sellerProfileImage);
    }

    private void launchChatActivity(int sellerId, String sellerName, String sellerProfileImage) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(BUNDLE_KEY_SELLER_ID, String.valueOf(sellerId));
        i.putExtra(BUNDLE_KEY_CHAT_TITLE, sellerName);
        i.putExtra(BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL, sellerProfileImage);
        context.startActivity(i);
    }

    public void onClickMinus(int qty) {
        if (qty <= QTY_ZERO) {
            data.setQuantity(QTY_ZERO);
        } else {
            qty--;
            data.setQuantity(qty);
        }
    }

    public void onClickPlus(int qty) {
        qty++;
        if (qty > QTY_MAX_ALLOWED) {
            qty--;
        }
        qty = isQuantityExceeding(qty);
        data.setQuantity(qty);
    }

    public boolean checkQuantity(int qty) {
        return qty == QTY_ZERO;
    }

    public void onClickEditext(int qty) {
        qty = isQuantityExceeding(qty);
        data.setQuantity(qty);
    }
}