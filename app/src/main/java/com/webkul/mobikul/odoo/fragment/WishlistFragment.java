package com.webkul.mobikul.odoo.fragment;

import android.content.Context;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CUSTOMER_FRAG_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.CustomerBaseActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.customer.WishlistProductInfoRvAdapter;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.databinding.FragmentWishlistBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CartUpdateListener;
import com.webkul.mobikul.odoo.helper.CustomerHelper;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.BaseResponse;
import com.webkul.mobikul.odoo.model.customer.wishlist.MyWishListResponse;
import com.webkul.mobikul.odoo.model.customer.wishlist.WishListData;
import com.webkul.mobikul.odoo.model.request.WishListToCartRequest;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */

public class WishlistFragment extends BaseFragment implements WishlistProductInfoRvAdapter.WishListInterface {
    private static final String TAG = "WishlistFragment";
    public FragmentWishlistBinding mBinding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    List<WishListData> wishListData;
    WishlistProductInfoRvAdapter.WishListInterface wishListInterface;
    WishlistProductInfoRvAdapter wishlistProductInfoRvAdapter;

    private CartUpdateListener cartUpdateListener;

    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        if(context instanceof CartUpdateListener) {
            cartUpdateListener = (CartUpdateListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        wishListInterface = this;
        callApi();
        setOnclickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        ApiConnection.getWishlist(getContext()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyWishListResponse>(getContext()) {

                    @Override
                    public void onNext(@NonNull MyWishListResponse myWishListResponse) {
                        super.onNext(myWishListResponse);
                        if (myWishListResponse.isAccessDenied()) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(getContext());
                                    Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, getActivity().getClass().getSimpleName());
                                    startActivity(i);
                                }
                            });



                } else {
                    getActivity().setTitle(getString(R.string.wishlist) + " (" + myWishListResponse.getWishLists().size() + ")");
                    mBinding.setData(myWishListResponse);
                    mBinding.executePendingBindings();
                    DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
                    mBinding.wishlistProductRv.addItemDecoration(dividerItemDecorationHorizontal);
                    wishListData = myWishListResponse.getWishLists();
                    wishlistProductInfoRvAdapter = new WishlistProductInfoRvAdapter(getContext(), myWishListResponse.getWishLists(), wishListInterface);
                    mBinding.wishlistProductRv.setAdapter(wishlistProductInfoRvAdapter);
                    if(cartUpdateListener!=null){
                        cartUpdateListener.updateCart();
                    }
                }
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
        Helper.showCartMenuItem(menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setOnclickListeners() {
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.home_nav_host);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        mBinding.btnContinueShopping.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.action_wishlistFragment_to_homeFragment);
            }else{
                Intent intent = new Intent(requireActivity(), NewHomeActivity.class);
                requireActivity().startActivity(intent);
            }

        });
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }

    @Override
    public void onDeleteProduct(Integer position) {
        deleteProduct(wishListData.get(position));
    }

    @Override
    public void addProductToBag(Integer pos) {
       addProduct(wishListData.get(pos));
    }

    public void deleteProduct(WishListData mData) {

        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getContext().getString(R.string.msg_are_you_sure), getContext().getString(R.string.ques_want_to_delete_this_product), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                AlertDialogHelper.showDefaultProgressDialog(getContext());

                ApiConnection.deleteWishlistItem(getContext(), mData.getId()).subscribeOn(Schedulers.io()).observeOn
                        (AndroidSchedulers.mainThread()).subscribe(new CustomObserver<BaseResponse>(getContext()) {

                    @Override
                    public void onNext(@NonNull BaseResponse baseResponse) {
                        super.onNext(baseResponse);
                        AlertDialogHelper.dismiss(getContext());
                        if (baseResponse.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getContext().getString(R.string.error_login_failure), getContext().getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(getContext());
                                    Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)getContext()).getClass().getSimpleName());
                                    getContext().startActivity(i);
                                }
                            });
                        }else {
                            if (baseResponse.isSuccess()) {
                                AnalyticsImpl.INSTANCE.trackItemRemovedFromWishlist(mData.getId(), mData.getName(), mData.getPriceUnit());
                                callApi();
                                CustomToast.makeText(getContext(), baseResponse.getMessage(), Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            } else {
                                AnalyticsImpl.INSTANCE.trackItemRemoveFromWishlistFailed(baseResponse.getMessage(), baseResponse.getResponseCode(), "");
                                AlertDialogHelper.showDefaultWarningDialog(getContext(), mData.getName(), baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
    }


    public void addProduct(WishListData mData) {
        AnalyticsImpl.INSTANCE.trackMoveToBagSelected(mData.getId(), mData.getName(), mData.getPriceUnit(),
                Helper.getScreenName(getContext()),
                "");
        ApiConnection.wishlistToCart(getContext(), new WishListToCartRequest(mData.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<BaseResponse>(getContext()) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(getContext());
                    }

                    @Override
                    public void onNext(@NonNull BaseResponse wishlistToCartResponse) {
                        super.onNext(wishlistToCartResponse);
                        if (wishlistToCartResponse.isAccessDenied()){
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getContext().getString(R.string.error_login_failure), getContext().getString(R.string.access_denied_message), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppSharedPref.clearCustomerData(getContext());
                                    Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                    i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity)getContext()).getClass().getSimpleName());
                                    getContext().startActivity(i);
                                }
                            });
                        }else {
                            FirebaseAnalyticsImpl.logAddToCartEvent(getContext(),mData.getProductId(),mData.getName());
                            if (wishlistToCartResponse.isSuccess()) {
                                AnalyticsImpl.INSTANCE.trackMoveItemToBagSuccessful(mData.getId(), mData.getName(), mData.getPriceUnit(),
                                        Helper.getScreenName(getContext()),
                                        "");

                                callApi();
                                AlertDialogHelper.showDefaultSuccessOneLinerDialog(getContext(), wishlistToCartResponse.getMessage());
                            } else {
                                AnalyticsImpl.INSTANCE.trackMoveItemToBagFailed(wishlistToCartResponse.getMessage(), wishlistToCartResponse.getResponseCode(), "");
                                AlertDialogHelper.showDefaultErrorDialog(getContext(), getContext().getString(R.string.add_to_bag),
                                        wishlistToCartResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}


