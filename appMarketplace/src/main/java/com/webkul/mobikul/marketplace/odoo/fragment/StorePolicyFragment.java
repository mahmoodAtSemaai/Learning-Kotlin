package com.webkul.mobikul.marketplace.odoo.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.adapter.StorePolicyViewPagerAdapter;
import com.webkul.mobikul.marketplace.odoo.databinding.FragmentStorePolicyBinding;
import com.webkul.mobikul.odoo.fragment.BaseFragment;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_RETURN_POLICY;
import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_SHIPPING_POLICY;

/**
 * Created by aastha.gupta on 1/11/17 in Mobikul_Odoo_Application.
 */

public class StorePolicyFragment extends BaseFragment {

    @SuppressWarnings("unused")
    private static final String TAG = "StorePolicyFragment";
    private FragmentStorePolicyBinding mBinding;

    public static StorePolicyFragment newInstance(String returnPolicy, String shippingPolicy) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_RETURN_POLICY, returnPolicy);
        args.putString(BUNDLE_KEY_SHIPPING_POLICY, shippingPolicy);
        StorePolicyFragment fragment = new StorePolicyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_policy, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getActivity().setTitle(getString(R.string.title_fragment_policies));
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        StorePolicyViewPagerAdapter storePolicyViewPagerAdapter = new StorePolicyViewPagerAdapter(getChildFragmentManager());
        storePolicyViewPagerAdapter.addFragment(WebviewFragment.newInstance(getArguments().getString(BUNDLE_KEY_RETURN_POLICY), null)
                , getString(R.string.return_policy));
        storePolicyViewPagerAdapter.addFragment(WebviewFragment.newInstance(getArguments().getString(BUNDLE_KEY_SHIPPING_POLICY), null)
                , getString(R.string.shipping_policy));
        mBinding.viewpager.setAdapter(storePolicyViewPagerAdapter);
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