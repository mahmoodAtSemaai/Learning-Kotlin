package com.webkul.mobikul.odoo.connection;


import android.annotation.SuppressLint;
import android.content.Context;

import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.database.SqlLiteDbHelper;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.NetworkHelper;
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
import com.webkul.mobikul.odoo.model.customer.address.MyAddressesResponse;
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
import com.webkul.mobikul.odoo.model.request.AddProductReviewRequest;
import com.webkul.mobikul.odoo.model.request.AddToBagRequest;
import com.webkul.mobikul.odoo.model.request.AddToWishlistRequest;
import com.webkul.mobikul.odoo.model.request.AuthenticationRequest;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;
import com.webkul.mobikul.odoo.model.request.CartToWishlistRequest;
import com.webkul.mobikul.odoo.model.request.CategoryRequest;
import com.webkul.mobikul.odoo.model.request.DeactivateRequest;
import com.webkul.mobikul.odoo.model.request.OrderReviewRequest;
import com.webkul.mobikul.odoo.model.request.PlaceOrderRequest;
import com.webkul.mobikul.odoo.model.request.ProductReviewRequest;
import com.webkul.mobikul.odoo.model.request.ProductSliderRequest;
import com.webkul.mobikul.odoo.model.request.RegisterDeviceTokenRequest;
import com.webkul.mobikul.odoo.model.request.ReviewLikeDislikeRequest;
import com.webkul.mobikul.odoo.model.request.SaveCustomerDetailRequest;
import com.webkul.mobikul.odoo.model.request.SearchRequest;
import com.webkul.mobikul.odoo.model.request.SignUpRequest;
import com.webkul.mobikul.odoo.model.request.UpdateBagReq;
import com.webkul.mobikul.odoo.model.request.WishListToCartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
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
            return RetrofitClient.getClient(context).create(ApiInterface.class).getHomePageData(new RegisterDeviceTokenRequest(context).toString());
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

    public static Observable<ProductData> getProductData(Context context, String productId) {

        return RetrofitClient.getClient(context).create(ApiInterface.class).getProductData(productId);


    }


    public static Observable<CatalogProductResponse> getProductSliderData(Context context, ProductSliderRequest productSliderRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getProductSliderData(productSliderRequest.getUrl().substring(1, productSliderRequest.getUrl().length()), productSliderRequest.toString());
    }

    public static Observable<CatalogProductResponse> getCategoryProducts(Context context, CategoryRequest categoryRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCategoryProducts(categoryRequest.toString());
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

    public static Observable<MyWishListResponse> getWishlist(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getWishlist();
    }


    public static Observable<OrderDetailResponse> getOrderDetailsData(Context context, String url) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getOrderDetailsData(url);
    }

    public static Observable<MyAddressesResponse> getAddressBookData(Context context, BaseLazyRequest baseLazyRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getAddressBookData(baseLazyRequest.toString());
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

    public static Observable<BagResponse> getCartData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCartData();
    }


    public static Observable<BaseResponse> updateCart(Context context, String lineId, int qty) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).updateCart(lineId, new UpdateBagReq(qty).toString());
    }

    public static Observable<BaseResponse> deleteCartItem(Context context, String lineId) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).deleteCartItem(lineId);
    }

    public static Observable<AddToCartResponse> addToCart(Context context, AddToBagRequest addToBagRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addToCart(addToBagRequest.toString());
    }

    public static Observable<BaseResponse> addToWishlist(Context context, AddToWishlistRequest
            addToWishlistRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).addToWishlist(addToWishlistRequest.toString());
    }

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


      /*-----------------------------------------------------------------------------------------------------------------------------------------------------------
        EXTRA API's
     ------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public static Observable<SplashScreenResponse> getSplashPageData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSplashPageData();
    }


    public static Observable<CountryStateData> getCountryStateData(Context context) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getCountryStateData();
    }

    public static Observable<CatalogProductResponse> getSearchResponse(Context context, SearchRequest searchRequest) {
        return RetrofitClient.getClient(context).create(ApiInterface.class).getSearchResponse(searchRequest.toString());
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

    public static  Observable<BaseResponse> deleteProfileImage(Context context){
        return  RetrofitClient.getClient(context).create(ApiInterface.class).deleteProfileImage();
    }
    public static  Observable<BaseResponse> deleteBannerImage(Context context){
        return  RetrofitClient.getClient(context).create(ApiInterface.class).deleteBannerImage();
    }

}
