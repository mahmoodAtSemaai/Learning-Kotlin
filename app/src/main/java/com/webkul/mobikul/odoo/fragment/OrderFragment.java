package com.webkul.mobikul.odoo.fragment;


import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.navigation.NavArgs;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.FragmentContainerActivity;
import com.webkul.mobikul.odoo.adapter.customer.OrderItemDetailAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.constant.BundleConstant;
import com.webkul.mobikul.odoo.databinding.FragmentOrderBinding;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.CalendarUtil;
import com.webkul.mobikul.odoo.helper.Helper;
import com.webkul.mobikul.odoo.model.checkout.OrderDataResponse;
import com.webkul.mobikul.odoo.model.payments.OrderPaymentData;
import com.webkul.mobikul.odoo.model.payments.PaymentStatusResponse;
import com.webkul.mobikul.odoo.model.payments.PendingPaymentData;
import com.webkul.mobikul.odoo.model.payments.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CHECKOUT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_ID;


public class    OrderFragment extends BaseFragment {

    private FragmentOrderBinding binding;
    private SweetAlertDialog dialog;
    private final int TIME_EXPIRED = 0;
    NavController navController;
    private final int TIME_MILLIS = 1000;
    private final int SECONDS_IN_A_MINUTE = 60;
    private final int MINUTES_IN_AN_HOUR = 60;
    private final int SECONDS_IN_AN_HOUR = 3600;
    private final int PENDING = 1;
    private final int COMPLETED = 2;
    private final int FAILED = 3;

    public static final String TAG = "OrderFragment";

    public static OrderFragment newInstance(String orderID, String toolbarText) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_ORDER_ID, orderID);
        if (!toolbarText.isEmpty())
            bundle.putString(BundleConstant.BUNDLE_KEY_CHECKOUT, toolbarText);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        buildDialog();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            navController = Navigation.findNavController(view);
            requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    navController.popBackStack();
                    navController.navigate(R.id.orderListFragment);
                }
            });
        }
        catch (Exception e){

        }


    }


    private void buildDialog() {
        dialog = AlertDialogHelper.getAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE,
                getString(R.string.fetching_order_details), "", false, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        int orderId = getOrderIdFromArguments();
        getOrderDetails(orderId);
    }

    private int getOrderIdFromArguments() {

        if(getArguments().getString(BUNDLE_KEY_ORDER_ID)!=null){
            return Integer.parseInt(getArguments().getString(BUNDLE_KEY_ORDER_ID));
        }
        else {
            assert getArguments() != null;
            return getArguments().getInt("orderid");
        }

    }


    private void getOrderDetails(int orderId) {
        ApiConnection.getOrderData(requireContext(), orderId, false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<OrderDataResponse>(requireContext()) {
            @Override
            public void onSubscribe(Disposable d) {
                super.onSubscribe(d);
                dialog.setTitleText(getString(R.string.fetching_order_details));
                dialog.show();
            }

            @Override
            public void onNext(OrderDataResponse orderDataResponse) {
                super.onNext(orderDataResponse);
                handleOrderDataResponse(orderDataResponse);
                setClickListeners(orderDataResponse);
                dialog.hide();
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                dialog.hide();
                showErrorDialog();
                binding.getRoot().setVisibility(View.INVISIBLE);
            }
        });
    }

    private void handleOrderDataResponse(OrderDataResponse orderDataResponse) {
        try{
            binding.setData(orderDataResponse);
            setTextOnViews(orderDataResponse);
            binding.executePendingBindings();
            setupRecyclerView(orderDataResponse);
            setViewGroupVisibility(orderDataResponse);
        }catch (Exception e){
        }

    }


    private void showErrorDialog() {
        dialog = AlertDialogHelper.getAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE,
                getString(R.string.error_something_went_wrong), getString(R.string.error_order_details)
                , false, false);
        dialog.setConfirmText(getString(R.string.ok));
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            dialog.dismiss();
            if(navController!=null) {
                navController.popBackStack();
                navController.navigate(R.id.orderListFragment);
            }else{
                requireActivity().finish();
            }
        });
        dialog.show();
    }



    private void setViewGroupVisibility(OrderDataResponse orderDataResponse) {
        if (orderDataResponse.getPaymentMode().equalsIgnoreCase(getString(R.string.cod_text))) {
            setVisibility(binding.dueDateDetail, View.GONE);
            setVisibility(binding.bankPaymentMethodDetail, View.GONE);
        } else {
            boolean isPaymentDone = orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.done))
                    || orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.completed))
                    || orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.paid_2));
            if (isPaymentDone) {
                setVisibility(binding.dueDateDetail, View.GONE);
                setText(binding.tvStatus, getString(R.string.completed));
                setVisibility(binding.bankPaymentMethodDetail, View.GONE);
                setVisibility(binding.purchaseDateDetail, View.VISIBLE);
            } else if (orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.time_expired_2))
                    || orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.failed_2))
                    || orderDataResponse.getPaymentStatus().equalsIgnoreCase(getString(R.string.error))
                    || orderDataResponse.getStatus().equalsIgnoreCase(getString(R.string.cancelled))) {
                setVisibility(binding.dueDateDetail, View.GONE);
                setText(binding.tvStatus, orderDataResponse.getStatus().equalsIgnoreCase(getString(R.string.cancelled)) ?
                        getString(R.string.cancelled) : getString(R.string.time_expired));
                setVisibility(binding.bankPaymentMethodDetail, View.GONE);
                setVisibility(binding.purchaseDateDetail, View.VISIBLE);
            } else {
                setVisibility(binding.purchaseDateDetail, View.GONE);
                setText(binding.tvStatus, getString(R.string.pending));
            }
        }
    }

    private void setText(TextView textView, String text) {
        textView.setText(text);
    }

    private void setTextOnViews(OrderDataResponse orderDataResponse) {
        String codText = getString(R.string.cod_text);
        String paymentMode = orderDataResponse.getPaymentMode().equalsIgnoreCase(codText) ?
                codText : orderDataResponse.getBank().getName() + " " + orderDataResponse.getPaymentMode();
        OrderPaymentData orderPaymentData = new OrderPaymentData(paymentMode, orderDataResponse.getAmountTotal(),
                "(" + orderDataResponse.getItems().size() + " " + getString(R.string.product) + ")", orderDataResponse.getPointsRedeemed(), orderDataResponse.getDelivery().getTotal(), "",
                orderDataResponse.getGrandTotal());
        binding.paymentDetails.setData(orderPaymentData);
    }


    private void setupRecyclerView(OrderDataResponse orderDataResponse) {
        DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL);
        binding.rvProductsInfo.addItemDecoration(dividerItemDecorationHorizontal);
        binding.rvProductsInfo.setAdapter(new OrderItemDetailAdapter(requireContext(), orderDataResponse.getItems()));
    }

    private void setClickListeners(OrderDataResponse orderDataResponse) {
        binding.tvAccountNumber.setOnClickListener(v -> {
            copyTextToClipBoard(binding.tvAccountNumber.getText().toString());
        });


        binding.btnPaymentStatus.setOnClickListener(v -> {
            callPaymentStatus(orderDataResponse.getOrderId());
        });
    }

    private void copyTextToClipBoard(String textTobeCopied) {
        ClipboardManager clipboard = getSystemService(requireContext(), ClipboardManager.class);
        ClipData clip = ClipData.newPlainText(getString(R.string.label), textTobeCopied);
        if (clipboard != null)
            clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), R.string.text_copied_message, Toast.LENGTH_SHORT).show();
    }

    private void callPaymentStatus(int orderId) {
        ApiConnection.getPaymentTransactionStatus(requireContext(), orderId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<PaymentStatusResponse>(requireContext()) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        dialog.setTitleText(getString(R.string.checking_payment_status));
                        dialog.show();
                    }

                    @Override
                    public void onNext(PaymentStatusResponse paymentStatusResponse) {
                        super.onNext(paymentStatusResponse);
                        handlePaymentStatusResponse(paymentStatusResponse, orderId);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        dialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.error_payment_status))
                                .setConfirmText(getString(R.string.ok))
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.hide();
                                });
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialog.hide();
                    }
                });
    }

    private void handlePaymentStatusResponse(PaymentStatusResponse paymentStatusResponse, int orderId) {
        int paymentStatus = paymentStatusResponse.getResult().getPaymentStatusCode();
        if (paymentStatus == COMPLETED) {
            startActivity(new Intent(requireActivity(), FragmentContainerActivity.class)
                    .putExtra(BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE, BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN)
                    .putExtra(BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN, PaymentStatusFragment.SHOW_PAYMENT_COMPLETE_MESSAGE));
        } else if (paymentStatus == FAILED)
            startActivity(new Intent(requireActivity(), FragmentContainerActivity.class)
                    .putExtra(BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE, BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN)
                    .putExtra(BundleConstant.BUNDLE_PAYMENT_STATUS_SCREEN, PaymentStatusFragment.SHOW_PAYMENT_FAILURE_MESSAGE));
        else if (paymentStatus == PENDING) {
            Result result = paymentStatusResponse.getResult();
            PendingPaymentData pendingPaymentData =
                    new PendingPaymentData(result.getExpireDate(), result.getExpireTime(),
                            binding.paymentDetails.tvTotalPayment.getText().toString(), result.getBank(), orderId, false);
            startActivity(new Intent(requireActivity(), FragmentContainerActivity.class)
                    .putExtra(BundleConstant.BUNDLE_KEY_FRAGMENT_TYPE, BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN)
                    .putExtra(BundleConstant.BUNDLE_PAYMENT_PENDING_SCREEN, pendingPaymentData));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Helper.hiderAllMenuItems(menu);
    }


    @androidx.annotation.NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@androidx.annotation.NonNull String title) {

    }

    private void setVisibility(View view, int gone) {
        view.setVisibility(gone);
    }

}
