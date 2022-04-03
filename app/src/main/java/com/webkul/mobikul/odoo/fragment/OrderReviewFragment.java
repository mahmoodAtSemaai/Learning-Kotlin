package com.webkul.mobikul.odoo.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.adapter.checkout.OrderReviewProductAdapter;
import com.webkul.mobikul.odoo.databinding.FragmentCheckoutOrderReviewBinding;
import com.webkul.mobikul.odoo.handler.checkout.OrderReviewFragmentHandler;
import com.webkul.mobikul.odoo.helper.ViewHelper;
import com.webkul.mobikul.odoo.model.checkout.OrderReviewResponse;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_ORDER_REVIEW_RESPONSE;



/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class OrderReviewFragment extends BaseFragment {

    public FragmentCheckoutOrderReviewBinding mBinding;

    public static final String TAG = "OrderReviewFragment";

    public static OrderReviewFragment newInstance(OrderReviewResponse orderReviewRequestData) {
        OrderReviewFragment OrderReviewFragment = new OrderReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_ORDER_REVIEW_RESPONSE, orderReviewRequestData);
        OrderReviewFragment.setArguments(args);
        return OrderReviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_order_review, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        OrderReviewResponse orderReviewResponse = getArguments().getParcelable(BUNDLE_KEY_ORDER_REVIEW_RESPONSE);
        if (orderReviewResponse != null) {
            mBinding.setData(orderReviewResponse);
            mBinding.setHandler(new OrderReviewFragmentHandler(getContext(), orderReviewResponse));
            mBinding.productRv.setAdapter(new OrderReviewProductAdapter(getContext(), orderReviewResponse.getItems()));
            mBinding.paymentAcquirer.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            ViewHelper.focusOnView(mBinding.container, mBinding.paymentAcquirer.getBottom());
                            mBinding.paymentAcquirer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }

    @NonNull
    @Override
    public String getTitle() {
        return TAG;
    }

    @Override
    public void setTitle(@NonNull String title) {

    }
}