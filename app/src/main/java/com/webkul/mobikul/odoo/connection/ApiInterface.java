package com.webkul.mobikul.odoo.connection;

import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.CategoriesServices.GET_PRODUCT_CATEGORIES;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.GET_PRODUCT;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_CATEGORY_PRODUCTS;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_GLOBAL_PRODUCTS_ENABLED;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_PRODUCTS_LIMIT;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_PRODUCTS_OFFSET;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_SEARCH_PRODUCTS;
import static com.webkul.mobikul.odoo.data.remoteSource.remoteServices.ProductServices.QUERY_SELLER_PRODUCTS;

import com.webkul.mobikul.odoo.data.response.ProductCategoriesResponse;
import com.webkul.mobikul.odoo.data.response.ProductListResponse;
import com.webkul.mobikul.odoo.data.response.models.CartProductsResponse;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.core.data.response.BaseResponseNew;
import com.webkul.mobikul.odoo.model.ReferralResponse;
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse;
import com.webkul.mobikul.odoo.model.cart.BagResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod;
import com.webkul.mobikul.odoo.model.checkout.OrderDataResponse;
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse;
import com.webkul.mobikul.odoo.model.chat.ChatConfigResponse;
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse;
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse;
import com.webkul.mobikul.odoo.data.response.models.OrderReviewResponse;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.customer.ResetPasswordResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressBodyParams.AddressAPIConstants;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.DistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.StateListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.SubDistrictListResponse;
import com.webkul.mobikul.odoo.model.customer.address.addressResponse.VillageListResponse;
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyHistoryResponse;
import com.webkul.mobikul.odoo.model.customer.loyalty.LoyaltyTermsResponse;
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
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse;
import com.webkul.mobikul.odoo.data.response.models.DeleteAllCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.DeleteCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.GetCartId;
import com.webkul.mobikul.odoo.data.response.models.GetCartResponse;
import com.webkul.mobikul.odoo.data.response.models.GetDiscountPriceResponse;
import com.webkul.mobikul.odoo.data.response.models.GetSelectedItemsPriceResponse;
import com.webkul.mobikul.odoo.data.response.models.GetWishListResponse;
import com.webkul.mobikul.odoo.data.response.models.UpdateCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    /*Catalog*/
    String MOBIKUL_CATALOG_HOME_PAGE_DATA = "home-page";
    String MOBIKUL_SAME_PRODUCT_FROM_DIFF_SELLERS = "v1/products";
    String MOBIKUL_CATALOG_PRODUCT_TEMPLATE_DATA = "product-products/{product_id}/product-templates/{product_template_id}";
    String MOBIKUL_PRODUCT_REVIEWS = "product/reviews";
    String MOBIKUL_ADD_PRODUCT_REVIEWS = "my/saveReview";
    String MOBIKUL_REVIEW_LIKE_DISLIKE = "review/likeDislike";

    /*Checkout*/
    String LOYALTY_CHECKOUT_MY_CART = "sale-order";
    String MOBIKUL_CHECKOUT_UPDATE_MY_CART = "sale-orders/{sale_order_id}/lines/{line_id}";
    String MOBIKUL_CHECKOUT_DELETE_CART_ITEM = "sale-orders/{sale_order_id}/lines/{line_id}";

    String MOBIKUL_CHECKOUT_ADD_TO_CART = "mobikul/mycart/addToCart";
    String MOBIKUL_CHECKOUT_EMPTY_CART = "mobikul/mycart/setToEmpty";
    String MOBIKUL_CHECKOUT_PAYMENT_ACQUIRERS = "mobikul/paymentAcquirers";
    String LOYALTY_CHECKOUT_ORDER_REVIEW = "v2/sale-orders/review";
    String LOYALTY_CHECKOUT_ORDER_REVIEW_V3 = "v3/sale-orders/review";
    String LOYALTY_CHECKOUT_PLACE_ORDER = "v1/sale-orders/place";
    String MOBIKUL_CHECKOUT_SALE_ORDERS = "sale-orders";
    String MOBIKUL_CHECKOUT_SHIPPING_METHODS = "/mobikul/ShippingMethods";
    String MOBIKUL_CHECKOUT_SHIPPING_METHODS_V1 = "/v1/delivery-carriers";

    //Cart APIs
    String CART_API_VERSION = "v1";
    String WISHLIST_API_VERSION = "v1";
    String SEMAAI_POINTS_API_VERSION = "v1";
    String CHECKOUT_API_VERSION = "v2";
    String ORDER_REVIEW_DATA_API_VERSION = "v3";
    String SHIPPING_METHODS_API_VERSION = "v1";


    String CART_APIS_PREFIX = CART_API_VERSION + "/carts";
    String PARTNER_ID = "partnerId";
    String CART_ID = "cart_id";
    String ORDER_LINES = "order-lines";
    String LINE_ID = "lineId";
    String ORDER_ID = "orderId";
    String LINE_IDS = "line_ids";
    String LINEIDS = "lineIds";
    String SALE_ORDERS = "sale-orders";
    String REVIEW = "review";
    String DELIVERY_CARRIERS = "delivery-carriers";
    String PRODUCT_WISHLIST = WISHLIST_API_VERSION + "/product-wishlists";


    String CHECK_IF_CART_EXISTS = CART_APIS_PREFIX;
    String CREATE_CART = CART_APIS_PREFIX;
    String GET_CART_API = CART_APIS_PREFIX + "/{" + CART_ID + "}";
    String ADD_TO_CART_API = CART_APIS_PREFIX + "/{" + CART_ID + "}";
    String UPDATE_BULK_ITEM_CART_API = CART_APIS_PREFIX + "/{" + CART_ID + "}";
    String UPDATE_CART_API = CART_APIS_PREFIX + "/{" + CART_ID + "}/" + ORDER_LINES + "/{" + LINE_ID + "}";
    String DELETE_CART_ITEM = CART_APIS_PREFIX + "/{" + CART_ID + "}" + "/" + ORDER_LINES + "/{" + LINE_ID + "}";
    String DELETE_ALL_CART_ITEM = CART_APIS_PREFIX + "/{" + CART_ID + "}" + "/" + ORDER_LINES;
    String REDEEM_SEMAAI_POINTS = SEMAAI_POINTS_API_VERSION + "/redeem-history/apply";
    String SELECTED_PRODUCTS_PRICE = CART_APIS_PREFIX + "/{" + CART_ID + "}/calculate";


    String GET_WISHLIST_API = PRODUCT_WISHLIST;
    String ADD_TO_WISHLIST_API = PRODUCT_WISHLIST;
    String REMOVE_FROM_WISHLIST_API = PRODUCT_WISHLIST;


    String GET_CHECKOUT_ITEMS_API = CHECKOUT_API_VERSION + "/" + SALE_ORDERS + "/" + "{" + ORDER_ID + "}";
    String GET_ORDER_REVIEW_DATA_API = ORDER_REVIEW_DATA_API_VERSION + "/" + SALE_ORDERS + "/" + REVIEW;
    String GET_SHIPPING_METHODS_API = SHIPPING_METHODS_API_VERSION + "/" + DELIVERY_CARRIERS;


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
    String LOYALTY_MANAGEMENT_REFERRAL_CODE = "res-user/{user_id}/res-partner";
    String LOYALTY_MANAGEMENT_POINTS = "redeem-history";
    String LOYALTY_VALIDATE_REFERRAL_CODE = "res-partner/{referral_code}";
    String LOYALTY_POINTS_HISTORY = "v1/redeem-history";
    String LOYALTY_TERMS_LIST = "loyalty-program/terms";
    String LOYALTY_TERM_DETAILS = "loyalty-program/terms/{term_id}";

    String LOYALTY_POINTS_HISTORY_USER_ID = "user_id";
    String LIMIT = "limit";
    String OFFSET = "offset";


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
    String MOBIKUL_SELLER_TERM_AND_CONDITION = "mobikul/marketplace/seller/terms";
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
    String MOBIKUL_GET_ORDER_DATA = "/v1/sale-orders/{order_id}";
    String MOBIKUL_GET_ORDER_DATA_V2 = "/v2/sale-orders/{order_id}";
    String MOBIKUL_UPDATE_ORDER_DATA = "/sale-orders/{order_id}";
    String MOBIKUL_ORDER_ID = "order_id";
    String MOBIKUL_BANK_ID = "bank_id";
    String POINTS_REDEEM = "points_redeem";

    /*Chat*/
    String CHAT_CREATE_CHANNEL = "/im_livechat/seller_add";
    String CHAT_UNREAD_COUNT = "/mail-channels/unread-messages";
    String CHAT_SESSION = "/im_livechat/chat_session";
    String CHAT_HISTORY = "/mail-channels/chats";


    /*OTP Based Login*/
    String PHONE_NUMBER = "phone_number";
    String VALIDATE_PHONE_NUMBER = "v1/user/{" + PHONE_NUMBER + "}";
    String GENERATE_OTP = "v1/user/{" + PHONE_NUMBER + "}/authenticate";
    String LOGIN_WITH_OTP_JWT_TOKEN = "v1/user/{" + PHONE_NUMBER + "}/authenticate/otp";
    String LOGIN_WITH_JWT_TOKEN = "v1/user/{" + PHONE_NUMBER + "}/login";

    //HTTP Methods
    String DELETE = "DELETE";

    /*OTP BASED SIGNUP*/
    String GENERATE_SIGN_UP_OTP = "otp";
    String SIGN_UP_WITH_OTP = "users/validate-and-register";
    String USER_ID = "user_id";
    String USER_ONBOARDING_STAGE = "stages";
    String ONBOARDING_STAGES = "onboardings";
    String CUSTOMER_GROUPS = "customer-groups";
    String USER_CUSTOMER_GROUP = "users/{"+ USER_ID +"}";
    String REFERRAL = "referral_code";
    String CUSTOMER_ID = "partner_id";
    String USER_DETAILS = "users/{" + USER_ID + "}/partners/{" + CUSTOMER_ID + "}";
    String USER_ADDRESS = "/addresses/{"+CUSTOMER_ID+"}";

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

    @GET(LOYALTY_MANAGEMENT_REFERRAL_CODE)
    Observable<ReferralResponse> getReferralCode(@Path("user_id") String user_id);

    @PUT(LOYALTY_MANAGEMENT_REFERRAL_CODE)
    Observable<ReferralResponse> generateReferralCode(@Path("user_id") String user_id);

    @GET(LOYALTY_VALIDATE_REFERRAL_CODE)
    Observable<ReferralResponse> validateReferralCode(@Path("referral_code") String referralCode);

    @GET(LOYALTY_MANAGEMENT_POINTS)
    Observable<ReferralResponse> getLoyaltyPoints(@Query("user_id") String user_id);


    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CHECKOUT API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    @GET(LOYALTY_CHECKOUT_MY_CART)
    Observable<BagResponse> getCartData(@Query(POINTS_REDEEM) Boolean pointsRedeem);

    @Deprecated
    @PUT(MOBIKUL_CHECKOUT_UPDATE_MY_CART)
    Observable<BaseResponse> updateCartV1(
            @Path("sale_order_id") int saleOrderId,
            @Path("line_id") String lineId,
            @Body String updateCartReqStr
    );

    @DELETE(MOBIKUL_CHECKOUT_DELETE_CART_ITEM)
    Observable<BaseResponse> deleteCartItem(
            @Path("sale_order_id") int saleOrderId,
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


    @POST(LOYALTY_CHECKOUT_ORDER_REVIEW)
    Observable<OrderReviewResponse> getOrderReviewData(
            @Body String orderReviewStrReqpointsRedeemed
    );


    @POST(LOYALTY_CHECKOUT_PLACE_ORDER)
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

    @GET(GET_PRODUCT)
    Observable<BaseResponseNew<ProductListResponse>> getProductSearchResponse(
            @Query(QUERY_SEARCH_PRODUCTS) String searchQuery,
            @Query(QUERY_GLOBAL_PRODUCTS_ENABLED) Boolean globalProductsEnabled ,
            @Query(QUERY_PRODUCTS_OFFSET) int offset,
            @Query(QUERY_PRODUCTS_LIMIT) int limit
    );

    @GET(GET_PRODUCT)
    Observable<BaseResponseNew<ProductListResponse>> getSellerProducts(
            @Query(QUERY_SELLER_PRODUCTS) int sellerId,
            @Query(QUERY_PRODUCTS_OFFSET) int offset,
            @Query(QUERY_PRODUCTS_LIMIT) int limit
    );

    @GET(GET_PRODUCT)
    Observable<BaseResponseNew<ProductListResponse>> getCategoryProductList(
            @Query(QUERY_CATEGORY_PRODUCTS) int categoryId,
            @Query(QUERY_PRODUCTS_OFFSET) int offset,
            @Query(QUERY_PRODUCTS_LIMIT) int limit
    );

    @GET(GET_PRODUCT_CATEGORIES)
    Observable<BaseResponseNew<ProductCategoriesResponse>> getCategories();

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
       Chat API's
    ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    @GET(CHAT_UNREAD_COUNT)
    Observable<ChatBaseResponse> getUnreadChatCount();

    @POST(CHAT_CREATE_CHANNEL)
    Observable<ChatBaseResponse<ChatCreateChannelResponse>> createChannel();

    @GET(CHAT_SESSION)
    Observable<ChatBaseResponse<ChatConfigResponse>> getChatUrl(
            @Query("channel_uuid") String channelUuid,
            @Query("seller_id") String sellerId,
            @Query("user_id") String userId
    );

    @GET(CHAT_HISTORY)
    Observable<ChatBaseResponse<List<ChatHistoryResponse>>> getChatHistory();


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
            @Path(MOBIKUL_ORDER_ID) int orderID,
            @Query(POINTS_REDEEM) Boolean pointsRedeem
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

    @GET(LOYALTY_POINTS_HISTORY)
    Observable<LoyaltyHistoryResponse> getLoyaltyPointsHistory(
            @Query(LOYALTY_POINTS_HISTORY_USER_ID) String user_id
            , @Query(LIMIT) int limit
            , @Query(OFFSET) int offset);

    @GET(LOYALTY_TERMS_LIST)
    Observable<LoyaltyTermsResponse> getLoyaltyTerms();

    @GET(LOYALTY_TERM_DETAILS)
    Observable<LoyaltyTermsResponse> getLoyaltyTermDetails(@Path("term_id") int bannerId);

    @GET(CHECK_IF_CART_EXISTS)
    Observable<CartBaseResponse<GetCartId>> checkIfCartExists(@Query(PARTNER_ID) int partnerId);

    @POST(CREATE_CART)
    Observable<CartBaseResponse<GetCartId>> createCart(@Query(PARTNER_ID) int partnerId);

    @GET(GET_CART_API)
    Observable<CartBaseResponse<GetCartResponse>> getCartDataV1(@Path(CART_ID) int cartId);

    @PUT(ADD_TO_CART_API)
    Observable<CartBaseResponse<CartProductsResponse>> addProductToCartV1(@Path(CART_ID) int cartId, @Body String products);

    @PUT(UPDATE_CART_API)
    Observable<CartBaseResponse<UpdateCartItemResponse>> updateCartItem(@Path(CART_ID) int cartId, @Path(LINE_ID) int lineId, @Body String quantity);

    @PUT(UPDATE_BULK_ITEM_CART_API)
    Observable<CartBaseResponse<CartProductsResponse>> updateBulkCartItems(@Path(CART_ID) int cartId, @Body String products);

    @PUT(DELETE_CART_ITEM)
    Observable<CartBaseResponse<DeleteCartItemResponse>> deleteCartItem(@Path(CART_ID) int cartId, @Path(LINE_ID) int lineId, @Body String quantity);

    @DELETE(DELETE_ALL_CART_ITEM)
    Observable<CartBaseResponse<DeleteAllCartItemResponse>> deleteAllCartItem(@Path(CART_ID) int cartId);


    @POST(ADD_TO_WISHLIST_API)
    Observable<WishListUpdatedResponse> addItemToWishListV1(@Body String wishlistRequestBody);

    @HTTP(method = DELETE, path = REMOVE_FROM_WISHLIST_API, hasBody = true)
    Observable<WishListUpdatedResponse> removeItemFromWishListV1(@Body String wishlistRequestBody);

    @GET(GET_WISHLIST_API)
    Observable<GetWishListResponse> getWishlistV1();


    @GET(REDEEM_SEMAAI_POINTS)
    Observable<CartBaseResponse<GetDiscountPriceResponse>> getDiscountPrice(@Query(ORDER_ID) int orderId, @Query(value = LINEIDS, encoded = true) String list);

    @GET(SELECTED_PRODUCTS_PRICE)
    Observable<CartBaseResponse<GetSelectedItemsPriceResponse>> getSelectedItemsPrice(@Path(CART_ID) int cartId, @Query(value = LINEIDS, encoded = true) String list);


    @GET(GET_CHECKOUT_ITEMS_API)
    Observable<CartBaseResponse<OrderDataResponse>> getSaleOrderDataV2(@Path(ORDER_ID) int orderId, @Query(value = LINEIDS, encoded = true) String lineIds);

    @POST(GET_ORDER_REVIEW_DATA_API)
    Observable <Response<CartBaseResponse<OrderReviewResponse>>> getOrderReviewDataV3(@Body String orderReviewRequest);

    @GET(GET_SHIPPING_METHODS_API)
    Observable <CartBaseResponse<ArrayList<ActiveShippingMethod>>> getActiveShippingMethodsV2(@Query(PARTNER_ID) String partnerID);
}