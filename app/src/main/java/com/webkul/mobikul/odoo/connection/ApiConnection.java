package com.webkul.mobikul.odoo.connection;


import android.annotation.SuppressLint;
import android.content.Context;

import com.webkul.mobikul.odoo.data.request.AddToWishListRequest;
import com.webkul.mobikul.odoo.data.request.CartProductsRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.request.GetDiscountPriceRequest;
import com.webkul.mobikul.odoo.data.request.SetQuantityRequest;
import com.webkul.mobikul.odoo.data.request.UpdateCartRequest;
import com.webkul.mobikul.odoo.data.response.models.CartProductsResponse;
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse;
import com.webkul.mobikul.odoo.data.response.models.DeleteAllCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.DeleteCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.GetCartId;
import com.webkul.mobikul.odoo.data.response.models.GetCartResponse;
import com.webkul.mobikul.odoo.data.response.models.GetDiscountPriceResponse;
import com.webkul.mobikul.odoo.data.response.models.GetSelectedItemsPriceResponse;
import com.webkul.mobikul.odoo.data.response.models.GetWishListResponse;
import com.webkul.mobikul.odoo.data.response.models.OrderReviewResponse;
import com.webkul.mobikul.odoo.data.response.models.UpdateCartItemResponse;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.ReferralResponse;
import com.webkul.mobikul.odoo.model.analytics.UserAnalyticsResponse;
import com.webkul.mobikul.odoo.model.cart.BagResponse;
import com.webkul.mobikul.odoo.model.catalog.CatalogProductResponse;
import com.webkul.mobikul.odoo.model.chat.ChatBaseResponse;
import com.webkul.mobikul.odoo.model.chat.ChatConfigResponse;
import com.webkul.mobikul.odoo.model.chat.ChatCreateChannelResponse;
import com.webkul.mobikul.odoo.model.chat.ChatHistoryResponse;
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod;
import com.webkul.mobikul.odoo.model.checkout.OrderDataResponse;
import com.webkul.mobikul.odoo.model.checkout.OrderPlaceResponse;
import com.webkul.mobikul.odoo.model.checkout.PaymentAcquirerResponse;
import com.webkul.mobikul.odoo.model.checkout.ShippingMethodResponse;
import com.webkul.mobikul.odoo.model.checkout.UpdateOrderRequest;
import com.webkul.mobikul.odoo.model.customer.ResetPasswordResponse;
import com.webkul.mobikul.odoo.model.customer.account.SaveCustomerDetailResponse;
import com.webkul.mobikul.odoo.model.customer.address.AddressFormResponse;
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
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
import com.webkul.mobikul.odoo.model.payments.TransferInstructionResponse;
import com.webkul.mobikul.odoo.model.product.AddToCartResponse;
import com.webkul.mobikul.odoo.model.product.ProductReviewResponse;
import com.webkul.mobikul.odoo.model.request.AddProductReviewRequest;
import com.webkul.mobikul.odoo.model.request.AddToBagRequest;
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;
import com.webkul.mobikul.odoo.model.request.CartToWishlistRequest;
import com.webkul.mobikul.odoo.model.request.DeactivateRequest;
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest;
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest;
import com.webkul.mobikul.odoo.model.request.ProductReviewRequest;
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest;
import com.webkul.mobikul.odoo.model.request.ReviewLikeDislikeRequest;
import com.webkul.mobikul.odoo.model.request.SaveCustomerDetailRequest;
import com.webkul.mobikul.odoo.model.request.SignUpRequest;
import com.webkul.mobikul.odoo.model.request.UpdateBagReq;
import com.webkul.mobikul.odoo.model.request.WishListToCartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class ApiConnection {


    @SuppressWarnings("unused")
    private static final String TAG = "ApiConnection";

  /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CATALOG API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static Observable<HomePageResponse> getHomePageData(Context context) {
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
        HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
        if (!NetworkHelper.isNetworkAvailable(context) && homePageResponse != null) {
//                callback.onSuccess(homePageResponse);
            return Observable.just(homePageResponse);
        } else {
            return RetrofitClient.getClient(context).create(ApiInterface.class).getHomePageData();
        }
    }

    public static void getHomePageDataResponse(Context context, CustomRetrofitCallback callback) {
        if (!NetworkHelper.isNetworkAvailable(context)) {
            SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
            HomePageResponse homePageResponse = sqlLiteDbHelper.getHomeScreenData();
            if (homePageResponse != null) {
                callback.onSuccess(homePageResponse);
            }
        }
        getHomePageData(context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<HomePageResponse>(context) {
            @Override
            public void onNext(HomePageResponse homePageResponse) {
                super.onNext(homePageResponse);
                callback.onSuccess(homePageResponse);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                callback.onError(t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        });
    }

    public static Observable<ProductData> getProductData(Context context, String productId, String productTemplateId) {

        return RetrofitClient.getClient(context).create(ApiInterface.class).getProductData(productId, productTemplateId);


    }


    public static Observable<CatalogProductResponse> getProductSliderData(Context context, int sliderId, int offset, int limit) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getProductSliderData(sliderId, offset, limit);
    }

    public static Observable<CatalogProductResponse> getCategoryProducts(Context context, String categoryId, int offset, int limit) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCategoryProducts(categoryId, offset, limit);
    }

    public static Observable<ProductReviewResponse> getProductReviews(Context context, ProductReviewRequest
            productReviewRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getProductReviews(productReviewRequest
                .toString());
    }

    public static Observable<BaseResponse> addNewProductReview(Context context, AddProductReviewRequest
            productReviewRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addNewProductReview(productReviewRequest
                .toString());
    }

    public static Observable<BaseResponse> reviewLikeDislike(Context context, ReviewLikeDislikeRequest
            reviewLikeDislikeRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).reviewLikeDislike(reviewLikeDislikeRequest.toString());
    }




    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
       CUSTOMER API's
    ------------------------------------------------------------------------------------------------------------------------------------------------------------*/


    public static Observable<LoginResponse> signIn(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).signIn(new RegisterDeviceTokenRequest(context).toString());
    }

    public static Observable<ResetPasswordResponse> forgotSignInPassword(Context context, String username) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).forgotSignInPassword(new AuthenticationRequest(username, "").getForgetPasswordRequestStr());
    }


    public static Observable<SignUpResponse> signUp(Context context, SignUpRequest signUpReqeust) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).signUp(signUpReqeust.toString());
    }


    public static Observable<MyOrderReponse> getOrders(Context context, BaseLazyRequest baseLazyRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrders(baseLazyRequest.toString());
    }

    @Deprecated
    public static Observable<MyWishListResponse> getWishlist(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getWishlist();
    }


    public static Observable<OrderDetailResponse> getOrderDetailsData(Context context, String url) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrderDetailsData(url);
    }

    public static Observable<MyAddressesResponse> getAddressBookData(Context context, BaseLazyRequest baseLazyRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getAddressBookData(baseLazyRequest.toString());
    }

    public static Observable<ReferralResponse> getReferralCode(Context context, String userId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getReferralCode(userId);
    }

    public static Observable<ReferralResponse> generateReferralCode(Context context, String userId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).generateReferralCode(userId);
    }

    public static Observable<ReferralResponse> validateReferralCode(Context context, String referralCode) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).validateReferralCode(referralCode);
    }

    public static Observable<ReferralResponse> getLoyaltyPoints(Context context, String userId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getLoyaltyPoints(userId);
    }

//    public static void getAddressFormData(String url, AuthenticationRequest authenticationRequest, RetrofitCallback<AddressFormResponse> callback) {
//        RetrofitClient.getClient(context).create(ApiInterface.class).getAddressFormData(url, authenticationRequest.toString()).enqueue(callback);
//    }


    public static Observable<AddressFormResponse> getAddressFormData(Context context, String url) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getAddressFormData(url);
    }

    public static Observable<BaseResponse> updateAddressFormData(Context context, String url, String addressFormDataStr) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateAddressFormData(url, addressFormDataStr);
    }


    public static Observable<BaseResponse> addNewAddress(Context context, String addressFormDataStr) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addNewAddress(addressFormDataStr);
    }

    public static Observable<BaseResponse> editAddress(Context context, String addressData, String addressUrl) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).editAddress(addressUrl, addressData);
    }

    public static Observable<BaseResponse> deleteAddress(Context context, String addressUrl) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteAddress(addressUrl);
    }

    public static Observable<BaseResponse> setDefaultShippingAddress(Context context, String addressId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).setDefaultShippingAddress(addressId);
    }

    public static Observable<BaseResponse> signOut(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).signOut(new RegisterDeviceTokenRequest(context).toString());
    }

    public static Observable<SaveCustomerDetailResponse> saveCustomerDetails(Context context, SaveCustomerDetailRequest saveCustomerDetailRequest) {
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
        SaveCustomerDetailResponse saveCustomerDetailResponse = sqlLiteDbHelper.getAccountScreenData();
        if (!NetworkHelper.isNetworkAvailable(context) && saveCustomerDetailResponse != null) {
//                callback.onSuccess(homePageResponse);
            return Observable.just(saveCustomerDetailResponse);
        } else {
            return RetrofitClient.getClient(context).create(ApiInterface.class).saveCustomerDetails(saveCustomerDetailRequest.toString());
        }


    }

    public static Observable<BaseResponse> deleteWishlistItem(Context context, String wishlistId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteWishlistItem(wishlistId);
    }

    public static Observable<BaseResponse> sendEmailVerificationLink(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).sendEmailVerificationLink("");
    }


    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        CHECKOUT API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static Observable<BagResponse> getCartData(Context context, Boolean pointsRedeem) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCartData(pointsRedeem);
    }


    public static Observable<BaseResponse> updateCart(Context context, int saleOrderId, String lineId, int qty) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateCartV1(saleOrderId, lineId, new UpdateBagReq(qty).toString());
    }

    public static Observable<BaseResponse> deleteCartItem(Context context, int saleOrderId, String lineId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteCartItem(saleOrderId, lineId);
    }

    public static Observable<AddToCartResponse> addToCart(Context context, AddToBagRequest addToBagRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addToCart(addToBagRequest.toString());
    }

    @Deprecated
    public static Observable<BaseResponse> addToWishlist(Context context, AddToWishlistRequest
            addToWishlistRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addToWishlist(addToWishlistRequest.toString());
    }

    @Deprecated
    public static Observable<BaseResponse> removeFromWishlist(Context context, String productId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteProductFromWishlist(productId);
    }

    public static Observable<BaseResponse> wishlistToCart(Context context, WishListToCartRequest
            wishlistToCartRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).wishlistToCart(wishlistToCartRequest
                .toString());
    }

    public static Observable<BaseResponse> cartToWishlist(Context context, CartToWishlistRequest
            cartToWishlistRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).cartToWishlist(cartToWishlistRequest
                .toString());
    }

    public static Observable<BaseResponse> emptyCart(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).emptyCart();
    }

    public static Observable<PaymentAcquirerResponse> getPaymentAcquirers(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getPaymentAcquirers();
    }

    public static Observable<OrderReviewResponse> getOrderReviewData(Context context, OrderReviewRequest orderReviewRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrderReviewData(orderReviewRequest.toString());
    }

    public static Observable<OrderPlaceResponse> placeOrder(Context context, PlaceOrderRequest placeOrderRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).placeOrder(placeOrderRequest.toString());
    }

    public static Observable<ShippingMethodResponse> getActiveShippings(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getActiveShippings();
    }

    public static Observable<TermAndConditionResponse> getTermAndCondition(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getTermAndCondition();
    }

    /*
    ANALYTICS API'S
     */

    public static Observable<UserAnalyticsResponse> getUserAnalytics(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getUserAnalyticsDetails(new RegisterDeviceTokenRequest(context).toString());
    }


      /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        EXTRA API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static Observable<SplashScreenResponse> getSplashPageData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSplashPageData();
    }


    public static Observable<CountryStateData> getCountryStateData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCountryStateData();
    }

    public static Observable<StateListResponse> getStates(Context context, int company_id) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getStates(company_id);
    }

    public static Observable<DistrictListResponse> getDistricts(Context context, int state_id) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getDistricts(state_id);
    }

    public static Observable<SubDistrictListResponse> getSubDistricts(Context context, int district_id) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSubDistricts(district_id);
    }

    public static Observable<VillageListResponse> getVillages(Context context, int sub_district_id) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getVillages(sub_district_id);
    }


    public static Observable<CatalogProductResponse> getSearchResponse(Context context, String keyword, int offset, int limit) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSearchResponse(keyword, offset, limit);
    }


    public static Observable<NotificationMessagesResponse> getNotificationMessages(Context context) {
        SqlLiteDbHelper sqlLiteDbHelper = new SqlLiteDbHelper(context);
        NotificationMessagesResponse notificationMessagesResponse = sqlLiteDbHelper.getNotificScreenData();
        if (!NetworkHelper.isNetworkAvailable(context) && notificationMessagesResponse != null) {
            notificationMessagesResponse.setAccessDenied(false);
            return Observable.just(notificationMessagesResponse);
        } else {
            return RetrofitClient.getClient(context).create(ApiInterface.class).getNotificationMessages();
        }
    }


    public static Observable<BaseResponse> updateNotificationMessage(Context context, String notificationId, boolean isRead) {
        String updateNoficationMessage = "";
        try {
            JSONObject jsonObject = new JSONObject();
            updateNoficationMessage = jsonObject.put("isRead", isRead).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateNotificationMessage(notificationId, updateNoficationMessage);
    }


    public static Observable<BaseResponse> deleteNotificationMessage(Context context, String notificationId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteNotificationMessage(notificationId);
    }


    @SuppressLint("HardwareIds")
    public static Observable<BaseResponse> registerDeviceToken(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).registerDeviceToken(new RegisterDeviceTokenRequest(context).toString());
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
            Chat API's
         ------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    public static Observable<ChatBaseResponse> getUnreadChatCount(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getUnreadChatCount();
    }


    public static Observable<ChatBaseResponse<ChatCreateChannelResponse>> createChannel(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).createChannel();
    }

    public static Observable<ChatBaseResponse<ChatConfigResponse>> getChatUrl(Context context, String channelUuid, String sellerId, String userId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getChatUrl(channelUuid, sellerId, userId);
    }

    public static Observable<ChatBaseResponse<List<ChatHistoryResponse>>> getChatHistory(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getChatHistory();
    }



  /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        OTHER API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static Observable<BaseResponse> deactivateAcountDetail(Context context, DeactivateRequest deactivateRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deactivateAcountDetail(deactivateRequest.toString());
    }

    public static Observable<BaseResponse> getDownloadRequestData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getDownloadRequestData();
    }

    public static Observable<BaseResponse> getDownloadData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getDownloadData();
    }

    public static Observable<BaseResponse> deleteProfileImage(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteProfileImage();
    }

    public static Observable<BaseResponse> deleteBannerImage(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteBannerImage();
    }

    public static Observable<PaymentAcquirersResponse> getPaymentAcquirers(Context context, int company_id) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getPaymentAcquirers(company_id);
    }

    public static Observable<PaymentAcquirerMethodResponse> getPaymentAcquirersMethods(Context context, int acquirerId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getPaymentAcquirerMethods(acquirerId);
    }


    public static Observable<PaymentAcquirerMethodProviderResponse> getPaymentAcquirerMethodProviders(Context context, int paymentMethodId, int paymentVendorId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getPaymentAcquirerMethodProviders(paymentVendorId, paymentMethodId);
    }

    public static Observable<PaymentTransactionResponse> createPayments(Context context, String paymentDetails) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).createPayment(paymentDetails);
    }

    public static Observable<OrderDataResponse> getOrderData(Context context, int orderId, Boolean usePoints) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrderData(orderId, usePoints);
    }

    public static Observable<MyOrderReponse> getSaleOrders(Context context, BaseLazyRequest baseLazyRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSaleOrders(baseLazyRequest.toString());
    }

    public static Observable<BaseResponse> updateOrderData(Context context, int orderId, UpdateOrderRequest updateOrderRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateOrderData(orderId, updateOrderRequest.toString());
    }

    public static Observable<PaymentStatusResponse> getPaymentTransactionStatus(Context context, int orderId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getPaymentTransactionStatus(orderId);
    }


    public static Observable<TransferInstructionResponse> getTransferInstruction(Context context, int bankId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getTransferInstruction(bankId);
    }

    public static Observable<LoyaltyHistoryResponse> getLoyaltyPointsHistory(Context context, String userId, int limit, int offset) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getLoyaltyPointsHistory(userId, limit, offset);
    }

    public static Observable<LoyaltyTermsResponse> getLoyaltyTerms(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getLoyaltyTerms();
    }

    public static Observable<LoyaltyTermsResponse> getLoyaltyTermDetails(Context context, int bannerId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getLoyaltyTermDetails(bannerId);
    }


    public static Observable<CartBaseResponse<GetCartResponse>> getCartData(Context context, int cartId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCartDataV1(cartId);
    }

    public static Observable<CartBaseResponse<CartProductsResponse>> addProductToCartV1(Context context, int cartId, CartProductsRequest cartProductsRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addProductToCartV1(cartId, cartProductsRequest.getRequestBody());
    }

    public static Observable<CartBaseResponse<UpdateCartItemResponse>> updateCartV1(Context context, int cartId, int lineId, UpdateCartRequest updateCartRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateCartItem(cartId, lineId, updateCartRequest.getRequestBody());
    }

    public static Observable<CartBaseResponse<CartProductsResponse>> updateBulkCartItemsV1(Context context, int cartId, CartProductsRequest cartProductsRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateBulkCartItems(cartId, cartProductsRequest.getRequestBody());
    }

    public static Observable<CartBaseResponse<GetCartId>> checkIfCartExists(Context context, int partnerId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).checkIfCartExists(partnerId);
    }

    public static Observable<CartBaseResponse<GetCartId>> createCart(Context context, int partnerId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).createCart(partnerId);
    }



    public static Observable<CartBaseResponse<DeleteCartItemResponse>> deleteItemFromCartV1(Context context, int cartId, int lineId, SetQuantityRequest setQuantityRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteCartItem(cartId, lineId, setQuantityRequest.getRequestBody());
    }

    public static Observable<CartBaseResponse<DeleteAllCartItemResponse>> deleteAllCartItems(Context context, int cartId){
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteAllCartItem(cartId);
    }

    public static Observable<WishListUpdatedResponse> addItemToWishListV1(Context context, AddToWishListRequest wishListRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addItemToWishListV1(wishListRequest.toString());
    }

    public static Observable<WishListUpdatedResponse> removeItemFromWishListV1(Context context, DeleteFromWishListRequest wishListRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).removeItemFromWishListV1(wishListRequest.toString());
    }

    public static Observable<GetWishListResponse> getWishlistV1(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getWishlistV1();
    }

    public static Observable<CartBaseResponse<GetDiscountPriceResponse>> getDiscountPrice(Context context, GetDiscountPriceRequest request){
        return RetrofitClient.getClient(context).create(ApiInterface.class).getDiscountPrice(request.getOrderId(), request.getLineIds().toString().replace(" ", ""));
    }

    public static Observable<CartBaseResponse<GetSelectedItemsPriceResponse>> getSelectedItemsPrice(Context context, int cartId, ArrayList<Integer> lineIds){
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSelectedItemsPrice(cartId, lineIds.toString().replace(" ", ""));
    }

    public static Observable<CartBaseResponse<OrderDataResponse>> getSaleOrderDataV2(Context context, int orderId, ArrayList<Integer> lineIds, boolean usePoints) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSaleOrderDataV2(orderId, lineIds.toString().replace(" ", ""));
    }

    public static Observable<Response<CartBaseResponse<OrderReviewResponse>>> getOrderReviewDataV3(Context context, OrderReviewRequest orderReviewRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrderReviewDataV3(orderReviewRequest.toString());
    }

    public static Observable<CartBaseResponse<ArrayList<ActiveShippingMethod>>> getActiveShippingMethodV2(Context context, String userId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getActiveShippingMethodsV2(userId);
    }

}
