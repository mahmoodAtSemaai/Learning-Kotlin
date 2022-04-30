package com.webkul.mobikul.odoo.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentAddProductReviewBinding;
import com.webkul.mobikul.odoo.handler.product.ProductReviewHandler;
import com.webkul.mobikul.odoo.model.product.Review;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class AddProductReviewFragment extends BaseFragment {

    private static final String TAG = "AddProductReviewFragment";
    private static final String TEMPLATE_ID = "templateId";
    public FragmentAddProductReviewBinding mBinding;

    public static AddProductReviewFragment newInstance(String templateId) {
        AddProductReviewFragment fragment = new AddProductReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEMPLATE_ID, templateId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_product_review, container, false);
        mBinding.setData(new Review(getActivity()));
        mBinding.setHandler(new ProductReviewHandler(getActivity(), getArguments().getString(TEMPLATE_ID)));
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
        return TAG;
    }

    @Override
    public void setTitle(@NonNull String title) {

    }
}
