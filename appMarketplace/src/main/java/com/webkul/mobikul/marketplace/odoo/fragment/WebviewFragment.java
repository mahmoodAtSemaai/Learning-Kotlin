package com.webkul.mobikul.marketplace.odoo.fragment;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.databinding.FragmentWebViewBinding;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_ACTIVITY_TITLE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_HTML_CONTENT;

/**
 * Created by aastha.gupta on 1/11/17 in Mobikul_Odoo_Application.
 */

public class WebviewFragment extends Fragment {

    private FragmentWebViewBinding mBinding;

    public static WebviewFragment newInstance(String description, String title) {
        WebviewFragment webviewFragment = new WebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_HTML_CONTENT, description);
        bundle.putString(BUNDLE_KEY_ACTIVITY_TITLE, title);
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false);
        // TODO  change background and textColor
//        mBinding.webview.setBackgroundColor(Color.TRANSPARENT);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getString(BUNDLE_KEY_ACTIVITY_TITLE) != null) {
            getActivity().setTitle(getArguments().getString(BUNDLE_KEY_ACTIVITY_TITLE));
        }
        mBinding.setData(getArguments().getString(BUNDLE_KEY_HTML_CONTENT));
//        mBinding.webview.loadDataWithBaseURL(null, getArguments().getString(BUNDLE_KEY_HTML_CONTENT), "text/html", Xml.Encoding.UTF_8.name(), null);
    }
}