package com.webkul.mobikul.marketplace.odoo.handler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.webkul.mobikul.marketplace.odoo.activity.BecomeSellerActivity;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.fragment.SignUpFragment;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.helper.FragmentHelper;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.customer.signin.LoginRequestData;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class MarketplaceLandingActivityHandler {
    private Context mContext;

    public MarketplaceLandingActivityHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void onClickViewSellerCollection(int sellerId) {
        Intent intent = new Intent(mContext, CatalogProductActivity.class);
        intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.SELLER_PRODUCTS);
        intent.putExtra(BUNDLE_KEY_SELLER_ID, String.valueOf(sellerId));
        mContext.startActivity(intent);
    }

    public void onClickViewSellerProfile(int sellerId) {
        Intent intent = new Intent(mContext, ((OdooApplication) mContext.getApplicationContext()).getSellerProfileActivity());
        intent.putExtra(BUNDLE_KEY_SELLER_ID, String.valueOf(sellerId));
        mContext.startActivity(intent);
    }

    public void onClickOpenAShop(){
        if(AppSharedPref.isLoggedIn(mContext)){
            if (!AppSharedPref.isSeller(mContext)){
                Intent intent = new Intent(mContext, BecomeSellerActivity.class);
                mContext.startActivity(intent);
            }

        }else {
            //FragmentHelper.addFragment(android.R.id.content, mContext, SignUpFragment.newInstance(), SignUpFragment.class.getSimpleName(), false, false);
            Intent intent = new Intent(mContext, SignInSignUpActivity.class);
            intent.putExtra("REQ_FOR", "Open new shop");
            mContext.startActivity(intent);

        }
    }
}
