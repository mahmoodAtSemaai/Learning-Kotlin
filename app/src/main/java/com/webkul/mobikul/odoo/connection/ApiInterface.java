package com.webkul.mobikul.odoo.connection;

import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.cart.BagResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.customer.ResetPasswordResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressRequestBody;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse;
import com.webkul.mobikul.odoo.model.customer.order.MyOrderReponse;
import com.webkul.mobikul.odoo.model.customer.order.OrderDetailResponse;
import com.webkul.mobikul.odoo.model.customer.signin.LoginResponse;
import com.webkul.mobikul.odoo.model.customer.signup.SignUpResponse;
import com.webkul.mobikul.odoo.model.customer.signup.TermAndConditionResponse;
import com.webkul.mobikul.odoo.model.customer.wishlist.MyWishListResponse;
import com.webkul.mobikul.odoo.model.extra.SplashScreenResponse;
import com.webkul.mobikul.odoo.model.generic.CountryStateData;
import com.webkul.mobikul.odoo.model.generic.ProductData;
import com.webkul.mobikul.odoo.model.home.HomePageResponse;
import com.webkul.mobikul.odoo.model.notification.NotificationMessagesResponse;
import com.webkul.mobikul.odoo.model.product.AddToCartResponse;
import com.webkul.mobikul.odoo.model.product.ProductReviewResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public interface ApiInterface {

    /*Catalog*/
    String MOBIKUL_CATALOG_HOME_PAGE_DATA = "mobikul/homepage";
    String MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA = "mobikul/template/{product_id}";
    String MOBIKUL_PRODUCT_REVIEWS = "product/reviews";
    String MOBIKUL_ADD_PRODUCT_REVIEWS = "my/saveReview";
    String MOBIKUL_REVIEW_LIKE_DISLIKE = "review/likeDislike";

    /*Checkout*/
    String MOBIKUL_CHECKOUT_MY_CART = "mobikul/mycart";
    String MOBIKUL_CHECKOUT_UPDATE_MY_CART = "mobikul/mycart/{line_id}";
    String MOBIKUL_CHECKOUT_DELETE_CART_ITEM = "mobikul/mycart/{line_id}";

    String MOBIKUL_CHECKOUT_ADD_TO_CART = "mobikul/mycart/addToCart";
    String MOBIKUL_CHECKOUT_EMPTY_CART = "mobikul/mycart/setToEmpty";
    String MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS = "mobikul/paymentAcquirers";
    String MOBIKUL_CHECKOUT_ORDER_REVIEW = "mobikul/orderReviewData";
    String MOBIKUL_CHECKOUT_PLACE_ORDER = "mobikul/placeMyOrder";
    String MOBIKUL_CHECKOUT_SHIPPING_METHODS = "/mobikul/ShippingMethods";

    /*Customer*/
    String MOBIKUL_CUSTOMER_SIGN_IN = "mobikul/customer/login";
    String MOBIKUL_CUSTOMER_FORGOT_PASSWORD = "mobikul/customer/resetPassword";
    String MOBIKUL_CUSTOMER_SIGN_UP = "mobikul/customer/signUp";
    String MOBIKUL_CUSTOMER_MY_ORDERS = "mobikul/my/orders";
    String MOBIKUL_CUSTOMER_MY_ADDRESSES = "mobikul/my/addresses";
    String MOBIKUL_CUSTOMER_ADD_NEW_ADDRESS = "mobikul/my/address/new";
    String MOBIKUL_CUSTOMER_DEFAULT_SHIPPING_ADDRESS = "mobikul/my/address/default/{address_id}";
    String MOBIKUL_CUSTOMER_SIGN_OUT = "mobikul/customer/signOut";
    String MOBIKUL_CUSTOMER_SAVE_DETAILS = "mobikul/saveMyDetails";
    String MOBIKUL_CUSTOMER_MY_WISHLIST = "mobikul/my/wishlists";
    String MOBIKUL_WHISHLIST_TO_CART = "my/wishlistToCart";
    String MOBIKUL_DELETE_WISHLIST_ITEM = "my/removeWishlist/{wishlist_id}";
    String MOBIKUL_ADD_TO_WHISHLIST = "my/addToWishlist";
    String MOBIKUL_DELETE_PRODUCT_FROM_WISHLIST = "my/removeFromWishlist/{product_id}";
    String MOBIKUL_CART_TO_WHISHLIST = "my/cartToWishlist";
    String MOBIKUL_SEND_EMAIL_VERIFICATION_LINK = "send/verifyEmail";

    /*Extras*/
    String MOBIKUL_EXTRAS_SPLASH_PAGE_DATA = "mobikul/splashPageData";
    String MOBIKUL_EXTRAS_COUNTRY_STATE_DATA = "mobikul/localizationData";
    String MOBIKUL_EXTRAS_STATE_DATA = "/states";
    String MOBIKUL_EXTRAS_DISTRICT_DATA = "/districts";
    String MOBIKUL_EXTRAS_SUB_DISTRICT_DATA = "/subdistricts";
    String MOBIKUL_EXTRAS_VILLAGE_DATA = "/villages";
    String MOBIKUL_EXTRAS_SEARCH = "mobikul/search";
    String MOBIKUL_EXTRAS_REGISTER_FCM_TOKEN = "mobikul/registerFcmToken";
    String MOBIKUL_EXTRAS_NOTIFICATION_MESSAGES = "mobikul/notificationMessages";
    String MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE = "mobikul/notificationMessage/{notification_id}";
    String MOBIKUL_DELETE_PROFILE_IMAGE = "/mobikul/deleteProfileImage";
    String MOBIKUL_DELETE_BANNER_IMAGE = "/mobikul/delete/customer/banner-image";


    /*GDPR Term and Condition*/
    String MOBIKUL_TERM_AND_CONDITION = "/mobikul/signup/terms";
    String MOBIKUL_GDPR_DEACTIVATE = "/mobikul/gdpr/deactivate";
    String MOBIKUL_GDPR_DOWNLOAD_REQUEST = "/mobikul/gdpr/downloadRequest";
    String MOBIKUL_GDPR_DOWNLOAD = "/mobikul/gdpr/download";
    String MOBIKUL_GDPR_DOWNLOAD_DATA = "/web/content/5520?download=true";

     /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CATALOG API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    @POST(MOBIKUL_CATALOG_HOME_PAGE_DATA)
    Observable<HomePageResponse> getHomePageData(@Body String registerDeviceTokenRequestStr);


    @POST(MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA)
    Observable<ProductData> getProductData(
            @Path("product_id") String productId
    );

    @POST
    Observable<CatalogProductResponse> getProductSliderData(
            @Url String url
            , @Body String productSliderRequestJsonStr
    );

    @POST(MOBIKUL_EXTRAS_SEARCH)
    Observable<CatalogProductResponse> getCategoryProducts(
            @Body String categoryRequestJsonDataStr
    );

    @POST(MOBIKUL_PRODUCT_REVIEWS)
    Observable<ProductReviewResponse> getProductReviews(
            @Body String productReviewRequest
    );

    @POST(MOBIKUL_ADD_PRODUCT_REVIEWS)
    Observable<BaseResponse> addNewProductReview(
            @Body String productReviewRequest
    );

    @POST(MOBIKUL_REVIEW_LIKE_DISLIKE)
    Observable<BaseResponse> reviewLikeDislike(
            @Body String reviewLikeDislikeRequest
    );

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CUSTOMER API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_CUSTOMER_SIGN_IN)
    Observable<LoginResponse> signIn(@Body String registerDeviceTokenRequestStr);


    @POST(MOBIKUL_CUSTOMER_SIGN_UP)
    Observable<SignUpResponse> signUp(
            @Body String signUpReqeustJsonStr
    );


    @POST(MOBIKUL_CUSTOMER_FORGOT_PASSWORD)
    Observable<ResetPasswordResponse> forgotSignInPassword(
            @Body String forgotSignInPasswordReqeustJsonStr
    );

    @POST(MOBIKUL_CUSTOMER_MY_ORDERS)
    Observable<MyOrderReponse> getOrders(
            @Body String baseLazyRequestStr
    );

    @POST(MOBIKUL_CUSTOMER_MY_WISHLIST)
    Observable<MyWishListResponse> getWishlist();

    @POST
    Observable<OrderDetailResponse> getOrderDetailsData(
            @Url String url
    );

    @POST(MOBIKUL_CUSTOMER_MY_ADDRESSES)
    Observable<MyAddressesResponse> getAddressBookData(
            @Body String baseRequestJsonStr
    );

    @POST
    Observable<AddressFormResponse> getAddressFormData(
            @Url String url
    );


    @PUT
    Observable<BaseResponse> updateAddressFormData(
            @Url String url
            , @Body String addressFormDataStr
    );

    @POST(MOBIKUL_CUSTOMER_ADD_NEW_ADDRESS)
    Observable<BaseResponse> addNewAddress(
            @Body String newAddressFormDataStr
    );

    @PUT
    Observable<BaseResponse> editAddress(@Url String url,@Body String addressData);

    @DELETE
    Observable<BaseResponse> deleteAddress(
            @Url String url
    );

    @PUT(MOBIKUL_CUSTOMER_DEFAULT_SHIPPING_ADDRESS)
    Observable<BaseResponse> setDefaultShippingAddress(
            @Path("address_id") String addressId
    );

    @POST(MOBIKUL_CUSTOMER_SIGN_OUT)
    Observable<BaseResponse> signOut(@Body String registerDeviceTokenRequestStr);

    @POST(MOBIKUL_CUSTOMER_SAVE_DETAILS)
    Observable<SaveCustomerDetailResponse> saveCustomerDetails(
            @Body String saveMyDetailReqStr
    );

    @DELETE(MOBIKUL_DELETE_WISHLIST_ITEM)
    Observable<BaseResponse> deleteWishlistItem(
            @Path("wishlist_id") String wishlistId
    );

    @POST(MOBIKUL_SEND_EMAIL_VERIFICATION_LINK)
    Observable<BaseResponse> sendEmailVerificationLink(
            @Body String string
    );

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CHECKOUT API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_CHECKOUT_MY_CART)
    Observable<BagResponse> getCartData();

    @PUT(MOBIKUL_CHECKOUT_UPDATE_MY_CART)
    Observable<BaseResponse> updateCart(
            @Path("line_id") String lineId,
            @Body String updateCartReqStr
    );

    @DELETE(MOBIKUL_CHECKOUT_DELETE_CART_ITEM)
    Observable<BaseResponse> deleteCartItem(
            @Path("line_id") String lineId
    );


    @POST(MOBIKUL_CHECKOUT_ADD_TO_CART)
    Observable<AddToCartResponse> addToCart(
            @Body String addToCartStrReq
    );

    @POST(MOBIKUL_WHISHLIST_TO_CART)
    Observable<BaseResponse> wishlistToCart(
            @Body String wishlistToCartStrReq
    );

    @POST(MOBIKUL_ADD_TO_WHISHLIST)
    Observable<BaseResponse> addToWishlist(
            @Body String addToWishlistStrReq
    );


    @DELETE(MOBIKUL_DELETE_PRODUCT_FROM_WISHLIST)
    Observable<BaseResponse> deleteProductFromWishlist(
            @Path("product_id") String productId
    );


    @POST(MOBIKUL_CART_TO_WHISHLIST)
    Observable<BaseResponse> cartToWishlist(
            @Body String cartToWishlistStrReq
    );

    @DELETE(MOBIKUL_CHECKOUT_EMPTY_CART)
    Observable<BaseResponse> emptyCart();


    @POST(MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS)
    Observable<PaymentAcquirerResponse> getPaymentAcquirers();


    @POST(MOBIKUL_CHECKOUT_ORDER_REVIEW)
    Observable<OrderReviewResponse> getOrderReviewData(
            @Body String orderReviewStrReq
    );


    @POST(MOBIKUL_CHECKOUT_PLACE_ORDER)
    Observable<OrderPlaceResponse> placeOrder(
            @Body String placeOrderRequest
    );

    @GET(MOBIKUL_CHECKOUT_SHIPPING_METHODS)
    Observable<ShippingMethodResponse> getActiveShippings();


    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        EXTRAS API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @POST(MOBIKUL_EXTRAS_SPLASH_PAGE_DATA)
    Observable<SplashScreenResponse> getSplashPageData();


    @POST(MOBIKUL_EXTRAS_COUNTRY_STATE_DATA)
    Observable<CountryStateData> getCountryStateData();


    @GET(MOBIKUL_EXTRAS_STATE_DATA)
    Observable<StateListResponse> getStates(@Query(AddressAPIConstants.COMPANY_ID) int company_id);

    @GET(MOBIKUL_EXTRAS_DISTRICT_DATA)
    Observable<DistrictListResponse> getDistricts(@Query(AddressAPIConstants.STATE_ID) int state_id);

    @GET(MOBIKUL_EXTRAS_SUB_DISTRICT_DATA)
    Observable<SubDistrictListResponse> getSubDistricts(@Query(AddressAPIConstants.DISTRICT_ID) int district_id);

    @GET(MOBIKUL_EXTRAS_VILLAGE_DATA)
    Observable<VillageListResponse> getVillages(@Query(AddressAPIConstants.SUBDISTRICT_ID) int sub_district_id);


    @POST(MOBIKUL_EXTRAS_SEARCH)
    Observable<CatalogProductResponse> getSearchResponse(
            @Body String searchJsonStr
    );

    @POST(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGES)
    Observable<NotificationMessagesResponse> getNotificationMessages();


    @POST(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE)
    Observable<BaseResponse> updateNotificationMessage(
            @Path("notification_id") String notificationId
            , @Body String updateNoficationMessage);


    @DELETE(MOBIKUL_EXTRAS_NOTIFICATION_MESSAGE)
    Observable<BaseResponse> deleteNotificationMessage(
            @Path("notification_id") String notificationId
    );

    @GET(MOBIKUL_TERM_AND_CONDITION)
    Observable<TermAndConditionResponse> getTermAndCondition();

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
       OTHER API's
    ------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    @POST(MOBIKUL_EXTRAS_REGISTER_FCM_TOKEN)
    Observable<BaseResponse> registerDeviceToken(
            @Body String registerDeviceTokenRequestStr
    );

    @POST(MOBIKUL_GDPR_DEACTIVATE)
    Observable<BaseResponse> deactivateAcountDetail(
            @Body String deactivateType
    );

    @POST(MOBIKUL_GDPR_DOWNLOAD_REQUEST)
    Observable<BaseResponse> getDownloadRequestData();

    @GET(MOBIKUL_GDPR_DOWNLOAD)
    Observable<BaseResponse> getDownloadData();

    @DELETE(MOBIKUL_DELETE_PROFILE_IMAGE)
    Observable<BaseResponse> deleteProfileImage();

    @DELETE(MOBIKUL_DELETE_BANNER_IMAGE)
    Observable<BaseResponse> deleteBannerImage();


}