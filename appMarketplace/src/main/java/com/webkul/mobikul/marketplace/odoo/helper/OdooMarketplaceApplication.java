package com.webkul.mobikul.marketplace.odoo.helper;

import android.content.Context;

import com.webkul.mobikul.marketplace.odoo.activity.AskToAdminActivity;
import com.webkul.mobikul.marketplace.odoo.activity.MarketplaceLandingActivity;
import com.webkul.mobikul.marketplace.odoo.activity.SellerDashBoardActivity;
import com.webkul.mobikul.marketplace.odoo.activity.SellerOrdersListActivity;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProfileActivity;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.handler.SingUpMarketplaceHandler;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerCollectionRequest;
import com.webkul.mobikul.odoo.adapter.customer.SignUpHandler;
import com.webkul.mobikul.odoo.databinding.FragmentSignUpBinding;
import com.webkul.mobikul.odoo.helper.OdooApplication;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpData;

import dagger.hilt.android.HiltAndroidApp;
import io.reactivex.Observable;

/**
 * Created by aastha.gupta on 18/9/17.
 */
@HiltAndroidApp
public class OdooMarketplaceApplication extends OdooApplication {

    public Observable<CatalogProductResponse> getSellerCollection(String sellerId, int offset, int itemsPerPage) {
        return MarketplaceApiConnection.getSellerCollectionData(this, sellerId, offset, itemsPerPage);
    }

    public Class getMarketplaceLandingPage() {
        return MarketplaceLandingActivity.class;
    }

    public Class getAskToAdminActivity() {
        return AskToAdminActivity.class;
    }

    public Class getSellerDashBoardPage() {
        return SellerDashBoardActivity.class;
    }

    public Class getSellerOrdersActivity() {
        return SellerOrdersListActivity.class;
    }

    @Override
    public SignUpHandler getSignUpHandler(Context context, SignUpData data, FragmentSignUpBinding mBinding) {
        return new SingUpMarketplaceHandler(context,data, mBinding);
    }
}
