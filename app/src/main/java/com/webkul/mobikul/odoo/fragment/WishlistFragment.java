package com.webkul.mobikul.odoo.fragment;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;
import static com.webkul.mobikul.odoo.core.utils.AppConstantsKt.HTTP_RESOURCE_CREATED;
import static com.webkul.mobikul.odoo.core.utils.AppConstantsKt.HTTP_RESOURCE_NOT_FOUND;
import static com.webkul.mobikul.odoo.core.utils.AppConstantsKt.HTTP_RESPONSE_OK;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.snackbar.Snackbar;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.BaseActivity;
import com.webkul.mobikul.odoo.activity.NewHomeActivity;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.customer.WishlistProductInfoRvAdapter;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.ApplicationConstant;
import com.webkul.mobikul.odoo.core.utils.AppConstantsKt;
import com.webkul.mobikul.odoo.custom.CustomToast;
import com.webkul.mobikul.odoo.data.request.CartProductItemRequest;
import com.webkul.mobikul.odoo.data.request.CartProductsRequest;
import com.webkul.mobikul.odoo.data.request.DeleteFromWishListRequest;
import com.webkul.mobikul.odoo.data.response.models.CartProductsResponse;
import com.webkul.mobikul.odoo.data.response.models.CartBaseResponse;
import com.webkul.mobikul.odoo.data.response.models.GetCartId;
import com.webkul.mobikul.odoo.data.response.models.GetWishListResponse;
import com.webkul.mobikul.odoo.data.response.models.WishListData;
import com.webkul.mobikul.odoo.data.response.models.WishListUpdatedResponse;
import com.webkul.mobikul.odoo.databinding.FragmentWishlistBinding;
import com.webkul.mobikul.odoo.firebase.FirebaseAnalyticsImpl;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.helper.CartUpdateListener;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.SnackbarHelper;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WishlistFragment extends Fragment implements WishlistProductInfoRvAdapter.WishListInterface {
    private static final String TAG = "WishlistFragment";
    public FragmentWishlistBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;

    ArrayList<WishListData> wishListData;
    WishlistProductInfoRvAdapter.WishListInterface wishListInterface;
    WishlistProductInfoRvAdapter wishlistProductInfoRvAdapter;
    SweetAlertDialog sweetAlertDialog;

    private CartUpdateListener cartUpdateListener;

    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wishlist, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        if (context instanceof CartUpdateListener) {
            cartUpdateListener = (CartUpdateListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initDialog();
        wishListInterface = this;
        callApi();
        setOnclickListeners();
    }

    private void initDialog() {
        sweetAlertDialog = AlertDialogHelper.getAlertDialog(requireContext(),
                SweetAlertDialog.PROGRESS_TYPE, getString(R.string.please_wait), "", false, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        callApi();
    }

    private void callApi() {
        ApiConnection.getWishlistV1(getContext()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<GetWishListResponse>(requireContext()) {

                    @Override
                    public void onNext(@NonNull GetWishListResponse myWishListResponse) {
                        super.onNext(myWishListResponse);
                        if (myWishListResponse.getStatusCode() > AppConstantsKt.HTTP_RESPONSE_OK) {
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
                            //Extend this fragment with our base fragment or create a new one else requireActivity and requireContext will crash
                            //https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
                            // How to rerpoduce it, 1) remove the below if block 2) Run the app
                            // 3) Head over to cart then click on wishlist icon 4) When wishlist APi is in progress press back
                            if(isAdded() && getActivity()!= null) {
                                binding.setData(myWishListResponse);
                                binding.executePendingBindings();
                                DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(requireActivity(), LinearLayout.VERTICAL);
                                binding.wishlistProductRv.addItemDecoration(dividerItemDecorationHorizontal);
                                wishListData = myWishListResponse.getResult();
                                wishlistProductInfoRvAdapter = new WishlistProductInfoRvAdapter(requireActivity(), myWishListResponse.getResult(), wishListInterface);
                                binding.wishlistProductRv.setAdapter(wishlistProductInfoRvAdapter);
                                if (cartUpdateListener != null) {
                                    cartUpdateListener.updateCart();
                                }
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

        binding.btnContinueShopping.setOnClickListener(v -> {
            if (navController != null) {
                navController.navigate(R.id.action_wishlistFragment_to_homeFragment);
            } else {
                Intent intent = new Intent(requireActivity(), NewHomeActivity.class);
                requireActivity().startActivity(intent);
            }

        });
    }

    @Override
    public void onDeleteProduct(Integer position) {
        deleteProductFromWishList(wishListData.get(position));
    }

    public void deleteProductFromWishList(WishListData mData) {

        AlertDialogHelper.showDefaultWarningDialogWithDismissListener(requireContext(), getString(R.string.msg_are_you_sure), getString(R.string.ques_want_to_delete_this_product), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                AlertDialogHelper.showDefaultProgressDialog(getContext());

                ApiConnection.removeItemFromWishListV1(getContext(), new DeleteFromWishListRequest(mData.getProductId()))
                        .subscribeOn(Schedulers.io()).observeOn
                        (AndroidSchedulers.mainThread()).subscribe(new CustomObserver<WishListUpdatedResponse>(requireContext()) {

                    @Override
                    public void onNext(@NonNull WishListUpdatedResponse response) {
                        super.onNext(response);
                        AlertDialogHelper.dismiss(getContext());
                        if (response.statusCode == AppConstantsKt.HTTP_ERROR_UNAUTHORIZED_REQUEST) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(requireContext(), getString(R.string.error_login_failure), requireContext().getString(R.string.access_denied_message), sweetAlertDialog1 -> {
                                sweetAlertDialog1.dismiss();
                                AppSharedPref.clearCustomerData(getContext());
                                Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity) getContext()).getClass().getSimpleName());
                                getContext().startActivity(i);
                            });
                        } else {
                            if (response.statusCode == AppConstantsKt.HTTP_RESPONSE_OK) {
                                AnalyticsImpl.INSTANCE.trackItemRemovedFromWishlist(String.valueOf(mData.getWishlistId()), mData.getName(), mData.getPriceUnit());
                                callApi();
                                CustomToast.makeText(getContext(), response.message, Toast.LENGTH_SHORT, R.style.GenericStyleableToast).show();
                            } else {
                                AnalyticsImpl.INSTANCE.trackItemRemoveFromWishlistFailed(response.message, response.statusCode, "");
                                AlertDialogHelper.showDefaultWarningDialog(getContext(), mData.getName(), response.message);
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


    @Override
    public void addProductToCart(Integer pos) {
        checkIfCartExists(pos);
    }

    private void checkIfCartExists(Integer pos) {
        int cartId = AppSharedPref.getCartId(requireContext());
        if (cartId == ApplicationConstant.CART_ID_NOT_AVAILABLE) {
            getCartId(wishListData.get(pos));
        } else {
            addProductToCart(cartId, wishListData.get(pos));
        }
    }

    private void getCartId(WishListData wishListData) {
        String customerId = AppSharedPref.getCustomerId(requireContext());
        ApiConnection.checkIfCartExists(requireContext(), Integer.parseInt(customerId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CartBaseResponse<GetCartId>>(requireContext()) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        sweetAlertDialog.show();
                    }

                    @Override
                    public void onNext(CartBaseResponse<GetCartId> response) {
                        if (response.getStatusCode() == HTTP_RESOURCE_NOT_FOUND)
                            createCart(Integer.parseInt(customerId), wishListData);
                        else {
                            AppSharedPref.setCartId(requireContext(), response.getResult().cartId);
                            sweetAlertDialog.dismiss();
                            addProductToCart(response.getResult().cartId, wishListData);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        createCart(Integer.parseInt(customerId), wishListData);
                    }
                });
    }

    private void createCart(int customerId, WishListData wishListData) {
        ApiConnection.createCart(requireContext(), customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CartBaseResponse<GetCartId>> (requireContext()) {

                    @Override
                    public void onNext(CartBaseResponse<GetCartId> response) {
                        super.onNext(response);
                        AppSharedPref.setCartId(requireContext(), response.getResult().cartId);
                        sweetAlertDialog.dismiss();
                        addProductToCart(response.getResult().cartId, wishListData);
                    }

                    @Override
                    public void onError(Throwable t) {
                        sweetAlertDialog.dismiss();
                        SnackbarHelper.getSnackbar(requireActivity(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
            });
    }

    public void addProductToCart(int cartId, WishListData mData) {
        ArrayList<CartProductItemRequest> list = new ArrayList<CartProductItemRequest>();
        list.add(new CartProductItemRequest(mData.getProductId(), 1, 0));

        ApiConnection.addProductToCartV1(requireContext(), cartId,new CartProductsRequest(list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<CartBaseResponse<CartProductsResponse>>(requireContext()) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                        AlertDialogHelper.showDefaultProgressDialog(getContext());
                    }

                    @Override
                    public void onNext(@NonNull CartBaseResponse<CartProductsResponse> response) {
                        super.onNext(response);
                        if (response.getStatusCode() == AppConstantsKt.HTTP_ERROR_UNAUTHORIZED_REQUEST) {
                            AlertDialogHelper.showDefaultWarningDialogWithDismissListener(getContext(), getString(R.string.error_login_failure), getString(R.string.access_denied_message), sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                AppSharedPref.clearCustomerData(getContext());
                                Intent i = new Intent(getContext(), SignInSignUpActivity.class);
                                i.putExtra(BUNDLE_KEY_CALLING_ACTIVITY, ((BaseActivity) getContext()).getClass().getSimpleName());
                                getContext().startActivity(i);
                            });
                        } else {
                            FirebaseAnalyticsImpl.logAddToCartEvent(getContext(), String.valueOf(mData.getProductId()), mData.getName());
                            if (response.getStatusCode() == HTTP_RESPONSE_OK ||
                                    response.getStatusCode() == HTTP_RESOURCE_CREATED) {
                                AppSharedPref.setNewCartCount(getContext(), response.getResult().getCartCount()); //update cart count after cart update
                                if (cartUpdateListener != null) {
                                    cartUpdateListener.updateCart();
                                }
                                AlertDialogHelper.showDefaultSuccessOneLinerDialog(getContext(), getString(R.string.add_to_bag), response.getMessage());
                            } else {
                                AlertDialogHelper.showDefaultErrorDialog(getContext(), getString(R.string.add_to_bag), response.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}


