package com.webkul.mobikul.marketplace.odoo.connection;

import android.content.Context;

import com.webkul.mobikul.marketplace.odoo.model.MarketplaceLandingPageData;
import com.webkul.mobikul.marketplace.odoo.model.SellerDashboardResponse;
import com.webkul.mobikul.marketplace.odoo.model.SellerReviewsResponse;
import com.webkul.mobikul.marketplace.odoo.model.request.AskToAdminRequest;
import com.webkul.mobikul.marketplace.odoo.model.request.BecomeSellerRequest;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerCollectionRequest;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerOrdersListRequest;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerProductsListRequest;
import com.webkul.mobikul.marketplace.odoo.model.request.SellerReviewRequest;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrderDetailsResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrdersListResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerProductListResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerProfilePageResponse;
import com.webkul.mobikul.odoo.connection.RetrofitClient;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.ReviewLikeDislikeResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;
import com.webkul.mobikul.odoo.model.request.ReviewLikeDislikeRequest;

import io.reactivex.Observable;

/**
 * Created by aastha.gupta on 20/9/17.
 */

public class MarketplaceApiConnection {

    public static Observable<SellerProfilePageResponse> getSellerProfileData(Context context, String sellerMarketplaceID) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerProfileData(sellerMarketplaceID);
    }

    public static Observable<CatalogProductResponse> getSellerCollectionData(Context context, SellerCollectionRequest sellerCollectionRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerCollectionData(sellerCollectionRequest.toString());
    }


    public static Observable<SellerReviewsResponse> getSellerReviewData(Context context, String sellerID) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerReviewData(sellerID);
    }

    public static Observable<SellerReviewsResponse> postSellerReviewData(Context context, String sellerID, SellerReviewRequest sellerReviewRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).postSellerReviewData(sellerID, sellerReviewRequest.toString());
    }

    public static Observable<MarketplaceLandingPageData> getLandingPageData(Context context) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getLandingPageData();
    }

    public static Observable<SellerDashboardResponse> getSellerDashboardData(Context context) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerDashboardData();
    }

    public static Observable<SellerProductListResponse> getSellerProductsList(Context context, SellerProductsListRequest sellerProductsListRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerProductsList(sellerProductsListRequest.toString());
    }

    public static Observable<SellerOrdersListResponse> getSellerOrdersList(Context context, SellerOrdersListRequest sellerOrdersListRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerOrdersList(sellerOrdersListRequest.toString());
    }

    public static Observable<SellerOrderDetailsResponse> getSellerOrderDetails(Context context, String LineID) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerOrderDetails(LineID);
    }

    public static Observable<BaseResponse> getAskAdmin(Context context, AskToAdminRequest askToAdminRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getAskAdmin(askToAdminRequest.toString());
    }
    public static Observable<TermAndConditionResponse> getSellerTerms(Context context) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).getSellerTerms();
    }

    public static Observable<BaseResponse> becomeSeller(Context context, BecomeSellerRequest becomeSellerRequest){
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).becomeSeller(becomeSellerRequest.toString());
    }

    public static Observable<ReviewLikeDislikeResponse> reviewLikeDislike(Context context, ReviewLikeDislikeRequest reviewLikeDislikeRequest) {
        return RetrofitClient.getClient(context).create(MarketplaceApiInterface.class).reviewLikeDislikeSeller(reviewLikeDislikeRequest.getmReviewId(),reviewLikeDislikeRequest.toString());
    }

}
