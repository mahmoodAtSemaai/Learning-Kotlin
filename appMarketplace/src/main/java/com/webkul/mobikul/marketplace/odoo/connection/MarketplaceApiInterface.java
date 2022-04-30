package com.webkul.mobikul.marketplace.odoo.connection;

import com.webkul.mobikul.marketplace.odoo.model.MarketplaceLandingPageData;
import com.webkul.mobikul.marketplace.odoo.model.SellerDashboardResponse;
import com.webkul.mobikul.marketplace.odoo.model.SellerReviewsResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrderDetailsResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerOrdersListResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerProductListResponse;
import com.webkul.mobikul.marketplace.odoo.model.response.SellerProfilePageResponse;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.ReviewLikeDislikeResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.webkul.mobikul.odoo.connection.ApiInterface.PRODUCTS_SEARCH;

/**
 * Created by aastha.gupta on 20/9/17.
 */

interface MarketplaceApiInterface {

    String MARKETPLACE_SELLER_PROFILE = "my/Template/seller/{seller_id}";
    String MARKETPLACE_SELLER_REVIEWS = "my/review/seller/{seller_id}";
    String MARKETPLACE_LANDING_PAGE = "mobikul/marketplace";
    String MARKETPLACE_SELLER_DASHBOARD = "mobikul/marketplace/seller/dashboard";
    String MARKETPLACE_SELLER_PRODUCTS = "mobikul/marketplace/seller/product";
    String MARKETPLACE_SELLER_ORDERS_LIST = "mobikul/marketplace/seller/orderlines";
    String MARKETPLACE_SELLER_ORDER_DETAILS = "mobikul/marketplace/seller/orderline/{id}";
    String MARKETPLACE_ASK_TO_ADMIN = "mobikul/marketplace/seller/ask";
    String MARKETPLACE_SELLER_TERMS = "mobikul/marketplace/seller/terms";
    String MARKETPLACE_BECOME_SELLER = "mobikul/marketplace/become/seller";
    String MARKETPLACE_LIKE_DISLIKE_SELLER_REVIEW = "mobikul/marketplace/seller/review/vote/{review_id}";


    @GET(MARKETPLACE_SELLER_PROFILE)
    Observable<SellerProfilePageResponse> getSellerProfileData(
            @Path("seller_id") String sellerID
    );

    @GET(PRODUCTS_SEARCH)
    Observable<CatalogProductResponse> getSellerCollectionData(
            @Query("seller_id") String sellerId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET(MARKETPLACE_SELLER_REVIEWS)
    Observable<SellerReviewsResponse> getSellerReviewData(
            @Path("seller_id") String sellerID
    );

    @POST(MARKETPLACE_SELLER_REVIEWS)
    Observable<SellerReviewsResponse> postSellerReviewData(
            @Path("seller_id") String sellerID,
            @Body String mSellerReviewRequest
    );

    @GET(MARKETPLACE_LANDING_PAGE)
    Observable<MarketplaceLandingPageData> getLandingPageData();

    @GET(MARKETPLACE_SELLER_DASHBOARD)
    Observable<SellerDashboardResponse> getSellerDashboardData();

    @POST(MARKETPLACE_SELLER_PRODUCTS)
    Observable<SellerProductListResponse> getSellerProductsList(
            @Body String sellerProductsListRequestJsonStr
    );

    @POST(MARKETPLACE_SELLER_ORDERS_LIST)
    Observable<SellerOrdersListResponse> getSellerOrdersList(
            @Body String SellerOrdersListRequestJsonStr
    );

    @GET(MARKETPLACE_SELLER_ORDER_DETAILS)
    Observable<SellerOrderDetailsResponse> getSellerOrderDetails(
            @Path("id") String LineID
    );

    @POST(MARKETPLACE_ASK_TO_ADMIN)
    Observable<BaseResponse> getAskAdmin(
            @Body String askToAdminRequest
    );

    @GET(MARKETPLACE_SELLER_TERMS)
    Observable<TermAndConditionResponse> getSellerTerms(
    );

    @POST(MARKETPLACE_BECOME_SELLER)
    Observable<BaseResponse> becomeSeller(
            @Body String becomeSellerRequest
    );

    @POST(MARKETPLACE_LIKE_DISLIKE_SELLER_REVIEW)
    Observable<ReviewLikeDislikeResponse> reviewLikeDislikeSeller(@Path("review_id") String reviewId,
                                                                  @Body String reviewLikeDislikeRequest
    );

}
