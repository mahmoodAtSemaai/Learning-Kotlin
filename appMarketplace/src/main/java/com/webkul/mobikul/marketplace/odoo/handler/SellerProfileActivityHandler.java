package com.webkul.mobikul.marketplace.odoo.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProfileActivity;
import com.webkul.mobikul.marketplace.odoo.databinding.ActivitySellerProfileBinding;
import com.webkul.mobikul.marketplace.odoo.fragment.SellerReviewFragment;
import com.webkul.mobikul.marketplace.odoo.fragment.StorePolicyFragment;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.ChatActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHAT_TITLE;

/**
 * Created by aastha.gupta on 19/9/17.
 */

public class SellerProfileActivityHandler {
    private static final String BACKSTACK_SUFFIX = "stack";
    private final int REQUEST_CODE_LOGIN = 10001;
    private Context mContext;
    private ActivitySellerProfileBinding binding;

    public SellerProfileActivityHandler(Context mContext, ActivitySellerProfileBinding binding) {
        this.mContext = mContext;
        this.binding = binding;
    }

    public void onClickViewSellerCollection(int sellerId) {
        Intent intent = new Intent(mContext, CatalogProductActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.SELLER_PRODUCTS);
        intent.putExtra(BUNDLE_KEY_SELLER_ID, String.valueOf(sellerId));
        mContext.startActivity(intent);
    }

    public void onClickViewSellerReview(int sellerID, int reviewDataSize) {
        if (reviewDataSize == 0) {
            SnackbarHelper.getSnackbar(((Activity) mContext), mContext.getString(R.string.seller_have_no_review_yet), Snackbar.LENGTH_LONG).show();
        } else {
            FragmentHelper.addFragment(R.id.container, mContext, SellerReviewFragment.newInstance(sellerID), SellerReviewFragment.class.getSimpleName(), true, true);
        }
    }


    public void onClickLogin(View v) {
        Intent intent = new Intent(mContext, SignInSignUpActivity.class);
        intent.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, HomeActivity.class.getSimpleName());
        ((SellerProfileActivity) mContext).startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    public void onClickViewSellerPolicies(String returnPolicy, String shippingPolicy) {
        if (returnPolicy.isEmpty() && shippingPolicy.isEmpty()) {
            SnackbarHelper.getSnackbar(((Activity) mContext), mContext.getString(R.string.no_policies_added_by_seller), Snackbar.LENGTH_LONG).show();
        } else {
            FragmentHelper.addFragment(R.id.container, mContext, StorePolicyFragment.newInstance(returnPolicy, shippingPolicy), StorePolicyFragment.class.getSimpleName(), true, true);
        }
    }

    public void onClickContactSellerBtn() {

    }

    public void onClickReadMore() {
        ViewGroup.LayoutParams params = binding.storeDescriptionWv.getLayoutParams();
        if (params.height == WRAP_CONTENT) {
            final float scale = mContext.getResources().getDisplayMetrics().density;
            params.height = (int) (30 * scale + 0.5f);
            binding.storeDescriptionWv.setLayoutParams(params);
            binding.readMoreTv.setText(R.string.read_more);
        } else {
            params.height = WRAP_CONTENT;
            binding.storeDescriptionWv.setLayoutParams(params);
            binding.readMoreTv.setText(R.string.read_less);
        }

    }

    public void onClickChatButton(int sellerId, String sellerName, String sellerProfileImage) {
        launchChatActivity(sellerId, sellerName, sellerProfileImage);
    }

    private void launchChatActivity(int sellerId, String sellerName, String sellerProfileImage){
        Intent i = new Intent(mContext, ChatActivity.class);
        i.putExtra(BUNDLE_KEY_SELLER_ID, String.valueOf(sellerId));
        i.putExtra(BUNDLE_KEY_CHAT_TITLE, sellerName);
        i.putExtra(BUNDLE_KEY_CHAT_PROFILE_PICTURE_URL, sellerProfileImage);
        mContext.startActivity(i);
    }
}
