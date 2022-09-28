package com.webkul.mobikul.odoo.handler.home;

import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CATEGORY;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_CUSTOM;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_LOYALTY_TERMS;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_NONE;
import static com.webkul.mobikul.odoo.constant.ApplicationConstant.TYPE_PRODUCT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_LOYALTY_BANNER_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_TEMPLATE_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_SEARCH_DOMAIN;
import static com.webkul.mobikul.odoo.helper.CatalogHelper.CatalogProductRequestType.SEARCH_DOMAIN;

import android.content.Context;
import android.content.Intent;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.LoyaltyTermsActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.model.generic.BannerImageData;
import com.webkul.mobikul.odoo.ui.price_comparison.ProductActivityV2;

/**
 * Created by shubham.agarwal on 27/12/16. @Webkul Software Pvt. Ltd
 */

public class HomeBannerHandler {


    @SuppressWarnings("unused")
    private static final String TAG = "HomeBannerHandler";
    private final Context mContext;
    private final BannerImageData mData;


    public HomeBannerHandler(Context context, BannerImageData bannerImageData) {
        mContext = context;
        mData = bannerImageData;
    }

    public void onClickBanner() {
        Intent intent = null;
        AnalyticsImpl.INSTANCE.trackPromotionalBannerSelected(mData.getId(), mData.getBannerName(), mContext.getString(R.string.home), 0);
        switch (mData.getBannerType()) {
            case TYPE_CUSTOM:
                intent = new Intent(mContext, CatalogProductActivity.class);
                intent.putExtra(BUNDLE_KEY_SEARCH_DOMAIN, mData.getDomain());
                intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, mData.getBannerName());
                intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, SEARCH_DOMAIN);
                break;
            case TYPE_PRODUCT:
                intent = new Intent(mContext, ProductActivityV2.class);
                intent.putExtra(BUNDLE_KEY_PRODUCT_ID, mData.getProductId());
                intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, mData.getBannerName());
                try {
                    intent.putExtra(BUNDLE_KEY_PRODUCT_TEMPLATE_ID, Integer.parseInt(mData.getId()));
                } catch (NumberFormatException ignored) {
                }
                break;
            case TYPE_CATEGORY:
                intent = new Intent(mContext, CatalogProductActivity.class);
                intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.BANNER_CATEGORY);
                intent.putExtra(BUNDLE_KEY_CATEGORY_ID, mData.getId());
                intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, mData.getBannerName());
                break;
            case TYPE_LOYALTY_TERMS:
                intent = new Intent(mContext, LoyaltyTermsActivity.class);
                intent.putExtra(BUNDLE_KEY_LOYALTY_BANNER_ID, Integer.parseInt(mData.getLoyaltyTermId()));
                break;
            case TYPE_NONE:
                //do nothing
                break;

        }
        if (intent != null) {
            mContext.startActivity(intent);
        }
    }
}