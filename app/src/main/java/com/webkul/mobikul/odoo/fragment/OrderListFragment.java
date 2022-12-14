package com.webkul.mobikul.odoo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.catalog.OrderRvAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentOrderListBinding;
import com.webkul.mobikul.odoo.handler.generic.EmptyFragmentHandler;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.helper.IntentHelper;
import com.webkul.mobikul.odoo.model.customer.order.MyOrderReponse;
import com.webkul.mobikul.odoo.model.request.BaseLazyRequest;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;


public class OrderListFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderListFragment";
    private FragmentOrderListBinding binding;
    private boolean mIsFirstCall = true;
    private int mOffset;
    private int pageSize = 10;
    NavController navController = null;



    public static OrderListFragment newInstance() {
        return new OrderListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list, container, false);
        setOffsetData();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            navController = Navigation.findNavController(view);

            requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    navController.navigateUp();
                }
            });
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

    }

    private void setOffsetData() {
        mIsFirstCall = true;
        mOffset = 0;
        callApi();
    }

    private void callApi() {
        ApiConnection.getSaleOrders(getContext(), new BaseLazyRequest(mOffset ,10)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<MyOrderReponse>(requireContext()) {

            @Override
            public void onNext(@NonNull MyOrderReponse myOrderReponse) {
                super.onNext(myOrderReponse);

                if (myOrderReponse.isAccessDenied()) {
                    IntentHelper.redirectToSignUpActivity(requireContext());
                } else {
                    handleOrdersResponse(myOrderReponse);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void handleOrdersResponse(MyOrderReponse myOrderReponse) {
        if (mIsFirstCall) {
            mIsFirstCall = false;
            binding.setData(myOrderReponse);

            /*BETTER REPLACE SOME CONTAINER INSTEAD OF WHOLE PAGE android.R.id.content */
            if (binding.getData().getOrders().isEmpty()) {
                binding.emptyLayout.setTitle(getString(R.string.no_order_placed));
                binding.emptyLayout.setSubtitle("");
                binding.emptyLayout.setEmptyImage(R.drawable.ic_vector_empty_order);
                binding.emptyLayout.setHideContinueShoppingBtn(true);
                binding.emptyLayout.setHandler(new EmptyFragmentHandler(getContext()));
            } else {

                binding.orderRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        if (!binding.getData().isLazyLoading() && lastCompletelyVisibleItemPosition == binding.orderRv.getAdapter().getItemCount() - 1 && binding.orderRv.getAdapter().getItemCount() < binding.getData().getTotalCount()) {
                            mOffset += pageSize;
                            callApi();
                            binding.getData().setLazyLoading(true);
                        }
                    }
                });
                binding.orderRv.setAdapter(new OrderRvAdapter(getContext(), binding.getData().getOrders(), TAG));
            }
        } else {
            binding.getData().setLazyLoading(false);
            binding.getData().getOrders().addAll(myOrderReponse.getOrders());
            binding.orderRv.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }

}
