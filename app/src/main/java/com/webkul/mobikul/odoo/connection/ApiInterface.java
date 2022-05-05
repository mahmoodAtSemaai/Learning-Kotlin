package com.webkul.mobikul.odoo.connection;

import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse;
import com.webkul.mobikul.odoo.model.cart.BagResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderDataResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.customer.ResetPasswordResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
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
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethodProviderResponse;
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethodResponse;
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirersResponse;
import com.webkul.mobikul.odoo.model.payments.PaymentStatusResponse;
import com.webkul.mobikul.odoo.model.payments.PaymentTransactionResponse;
import com.webkul.mobikul.odoo.model.payments.PaymentsAPIConstants;
import com.webkul.mobikul.odoo.model.payments.TransferInstructionResponse;
import com.webkul.mobikul.odoo.model.product.AddToCartResponse;
import com.webkul.mobikul.odoo.model.product.ProductReviewResponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    /*Catalog*/
    String MOBIKUL_CATALOG_HOME_PAGE_DATA = "home-page";
    String MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA = "product-products/{product_id}/product-templates/{product_template_id}";
    String MOBIKUL_PRODUCT_REVIEWS = "product/reviews";
    String MOBIKUL_ADD_PRODUCT_REVIEWS = "my/saveReview";
    String MOBIKUL_REVIEW_LIKE_DISLIKE = "review/likeDislike";

    /*Checkout*/
    String MY_CART = "cart";
    String MOBIKUL_CHECKOUT_UPDATE_MY_CART = "mobikul/mycart/{line_id}";
    String MOBIKUL_CHECKOUT_DELETE_CART_ITEM = "mobikul/mycart/{line_id}";

    String MOBIKUL_CHECKOUT_ADD_TO_CART = "mobikul/mycart/addToCart";
    String MOBIKUL_CHECKOUT_EMPTY_CART = "mobikul/mycart/setToEmpty";
    String MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS = "mobikul/paymentAcquirers";
    String MOBIKUL_CHECKOUT_ORDER_REVIEW = "sale-orders/review";
    String MOBIKUL_CHECKOUT_PLACE_ORDER = "sale-orders/place";
    String MOBIKUL_CHECKOUT_SALE_ORDERS = "sale-orders";
    String MOBIKUL_CHECKOUT_SHIPPING_METHODS = "/mobikul/ShippingMethods";

    /*Customer*/
    String MOBIKUL_CUSTOMER_SIGN_IN = "mobikul/customer/login";
    String MOBIKUL_CUSTOMER_FORGOT_PASSWORD = "mobikul/customer/resetPassword";
    String MOBIKUL_CUSTOMER_SIGN_UP = "res-user";
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

    /*Analytics*/
    String MOBIKUL_ANALYTICS = "/mobikul/analytics";

    /*Extras*/
    String MOBIKUL_EXTRAS_SPLASH_PAGE_DATA = "mobikul/splashPageData";
    String MOBIKUL_EXTRAS_COUNTRY_STATE_DATA = "mobikul/localizationData";
    String MOBIKUL_EXTRAS_STATE_DATA = "/states";
    String MOBIKUL_EXTRAS_DISTRICT_DATA = "/districts";
    String MOBIKUL_EXTRAS_SUB_DISTRICT_DATA = "/subdistricts";
    String MOBIKUL_EXTRAS_VILLAGE_DATA = "/villages";
    String MOBIKUL_EXTRAS_SEARCH = "mobikul/search";
    String PRODUCTS_SEARCH = "product-templates";
    String PRODUCT_SLIDER_DATA = "product-products";

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

    /*Payments*/
    String MOBIKUL_PAYMENTS_ACQUIRERS = "payment/acquirers";
    String MOBIKUL_PAYMENTS_ACQUIRERS_METHODS = "payment/acquirers/{" + PaymentsAPIConstants.ACQUIRER_ID + "}/methods";
    String MOBIKUL_PAYMENTS_ACQUIRERS_METHODS_PROVIDERS = "payment/acquirers/{" + PaymentsAPIConstants.ACQUIRER_ID + "}/methods/{" + PaymentsAPIConstants.PAYMENT_METHOD_ID + "}/providers";
    String MOBIKUL_CREATE_PAYMENTS_TRANSACTIONS = "/payment-transactions";
    String MOBIKUL_GET_PAYMENTS_TRANSACTIONS = "/payment-transactions";
    String MOBIKUL_GET_PAYMENTS_INSTRUCTIONS = "/payment/method-providers/{bank_id}/instructions";
    String MOBIKUL_GET_ORDER_DATA = "/sale-orders/{order_id}";
    String MOBIKUL_UPDATE_ORDER_DATA = "/sale-orders/{order_id}";
    String MOBIKUL_ORDER_ID = "order_id";
    String MOBIKUL_BANK_ID = "bank_id";

     /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CATALOG API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    @GET(MOBIKUL_CATALOG_HOME_PAGE_DATA)
    Observable<HomePageResponse> getHomePageData();


    @GET(MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA)
    Observable<ProductData> getProductData(
            @Path("product_id") String productId,
            @Path("product_template_id") String productTemplateId
    );

    @GET(PRODUCT_SLIDER_DATA)
    Observable<CatalogProductResponse> getProductSliderData(
            @Query("slider_id") int sliderId
            , @Query("offset") int offset
            , @Query("limit") int limit
    );

    @GET(PRODUCTS_SEARCH)
    Observable<CatalogProductResponse> getCategoryProducts(
            @Query("cid") String categoryId,
            @Query("offset") int offset,
            @Query("limit") int limit
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
    Observable<BaseResponse> editAddress(@Url String url, @Body String addressData);

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


    @GET(MY_CART)
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


    @GET(PRODUCTS_SEARCH)
    Observable<CatalogProductResponse> getSearchResponse(
            @Query("search") String keyword,
            @Query("offset") int offset,
            @Query("limit") int limit
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


    /*  Analytics API */
    @POST(MOBIKUL_ANALYTICS)
    Observable<UserAnalyticsResponse> getUserAnalyticsDetails(@Body String registerDeviceTokenRequestStr);


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

    @GET(MOBIKUL_PAYMENTS_ACQUIRERS)
    Observable<PaymentAcquirersResponse> getPaymentAcquirers(@Query(PaymentsAPIConstants.COMPANY_ID) int companyId);

    @GET(MOBIKUL_PAYMENTS_ACQUIRERS_METHODS)
    Observable<PaymentAcquirerMethodResponse> getPaymentAcquirerMethods(@Path(PaymentsAPIConstants.ACQUIRER_ID) int acquirerId);

    @GET(MOBIKUL_PAYMENTS_ACQUIRERS_METHODS_PROVIDERS)
    Observable<PaymentAcquirerMethodProviderResponse> getPaymentAcquirerMethodProviders(@Path(PaymentsAPIConstants.ACQUIRER_ID) int acquirerId, @Path(PaymentsAPIConstants.PAYMENT_METHOD_ID) int paymentMethodId);

    @POST(MOBIKUL_CREATE_PAYMENTS_TRANSACTIONS)
    Observable<PaymentTransactionResponse> createPayment(@Body String payment);

    @GET(MOBIKUL_GET_ORDER_DATA)
    Observable<OrderDataResponse> getOrderData(
            @Path(MOBIKUL_ORDER_ID) int orderID
    );

    @POST(MOBIKUL_CHECKOUT_SALE_ORDERS)
    Observable<MyOrderReponse> getSaleOrders(
            @Body String baseLazyRequestStr
    );

    @PUT(MOBIKUL_UPDATE_ORDER_DATA)
    Observable<BaseResponse> updateOrderData(@Path(MOBIKUL_ORDER_ID) int orderId, @Body String updateOrderRequest);

    @GET(MOBIKUL_GET_PAYMENTS_TRANSACTIONS)
    Observable<PaymentStatusResponse> getPaymentTransactionStatus(@Query(MOBIKUL_ORDER_ID) int orderId);

    @GET(MOBIKUL_GET_PAYMENTS_INSTRUCTIONS)
    Observable<TransferInstructionResponse> getTransferInstruction(@Path(MOBIKUL_BANK_ID) int bankId);

}