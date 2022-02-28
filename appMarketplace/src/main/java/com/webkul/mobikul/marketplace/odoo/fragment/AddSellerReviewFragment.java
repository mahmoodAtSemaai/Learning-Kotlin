package com.webkul.mobikul.marketplace.odoo.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.databinding.FragmentAddSellerReviewBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerReviewHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerReviewForm;
import com.webkul.mobikul.odoo.fragment.BaseFragment;

/**
 * Created by aastha.gupta on 5/3/18 in Mobikul_Odoo_Application.
 */

public class AddSellerReviewFragment extends BaseFragment {
    private static final String TAG = "AddProductReviewFragment";
    private static final String SELLER_ID = "seller_id";
    public FragmentAddSellerReviewBinding mBinding;

    public static AddSellerReviewFragment newInstance(int sellerId) {
        AddSellerReviewFragment fragment = new AddSellerReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELLER_ID, sellerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_seller_review, container, false);
        mBinding.setData(new SellerReviewForm(getActivity()));
        mBinding.setHandler(new SellerReviewHandler(getActivity(), getArguments().getInt(SELLER_ID)));
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setTitle(@NonNull String title) {

    }

}
