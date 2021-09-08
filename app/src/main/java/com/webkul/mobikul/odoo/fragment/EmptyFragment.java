package com.webkul.mobikul.odoo.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.FragmentEmptyBinding;
import com.webkul.mobikul.odoo.handler.generic.EmptyFragmentHandler;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_EMPTY_FRAGMENT_TYPE;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */
public class EmptyFragment extends BaseFragment {
    @SuppressWarnings("unused")
    private static final String TAG = "EmptyFragment";
    private FragmentEmptyBinding mBinding;

    public static EmptyFragment newInstance(int drawableId, String title, String subtitle, boolean hideContinueShoppingBtn, int emptyFragType) {
        EmptyFragment emptyFragment = new EmptyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID, drawableId);
        bundle.putString(BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID, title);
        bundle.putString(BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID, subtitle);
        bundle.putBoolean(BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN, hideContinueShoppingBtn);
        bundle.putInt(BUNDLE_KEY_EMPTY_FRAGMENT_TYPE, emptyFragType);
        emptyFragment.setArguments(bundle);
        return emptyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_empty, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setHideContinueShoppingBtn(getArguments().getBoolean(BUNDLE_KEY_EMPTY_FRAGMENT_HIDE_CONTINUE_SHOPPING_BTN));
        mBinding.setEmptyImage(getArguments().getInt(BUNDLE_KEY_EMPTY_FRAGMENT_DRAWABLE_ID));
        mBinding.setTitle(getArguments().getString(BUNDLE_KEY_EMPTY_FRAGMENT_TITLE_ID));
        mBinding.setSubtitle(getArguments().getString(BUNDLE_KEY_EMPTY_FRAGMENT_SUBTITLE_ID));
        mBinding.setHandler(new EmptyFragmentHandler(getContext()));
    }

    public enum EmptyFragType {
        TYPE_NOTIFICATION, TYPE_CART, TYPE_CATALOG_PRODUCT, TYPE_WISHLIST, TYPE_ORDER
    }


}
