package com.webkul.mobikul.marketplace.odoo.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.marketplace.odoo.R;
import com.webkul.mobikul.marketplace.odoo.activity.SellerProfileActivity;
import com.webkul.mobikul.marketplace.odoo.adapter.SellerReviewAdapter;
import com.webkul.mobikul.marketplace.odoo.connection.MarketplaceApiConnection;
import com.webkul.mobikul.marketplace.odoo.databinding.FragmentSellerReviewBinding;
import com.webkul.mobikul.marketplace.odoo.handler.SellerReviewHandler;
import com.webkul.mobikul.marketplace.odoo.model.SellerReviewsResponse;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.fragment.BaseFragment;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.marketplace.odoo.constant.MarketplaceBundleConstant.BUNDLE_KEY_SELLER_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;

/**
 * Created by aastha.gupta on 2/11/17 in Mobikul_Odoo_Application.
 */

public class SellerReviewFragment extends BaseFragment {

    private FragmentSellerReviewBinding mBinding;
    private static final String TAG = "SellerReviewFragment";

    public static SellerReviewFragment newInstance(int sellerID) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_SELLER_ID, sellerID);
        SellerReviewFragment fragment = new SellerReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSellerReviewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MarketplaceApiConnection.getSellerReviewData(getActivity(), String.valueOf(getArguments()
                .getInt(BUNDLE_KEY_SELLER_ID))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CustomObserver<SellerReviewsResponse>(getActivity()) {

            @Override
            public void onNext(@NonNull SellerReviewsResponse response) {
                super.onNext(response);

                if (response.isAccessDenied()){
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
                }else {
                    if (response.isSuccess()) {
                        mBinding.reviewRecyclerview.setAdapter(new SellerReviewAdapter(getActivity(), response.getSellerReview()));
                    }
                    mBinding.setHandler(new SellerReviewHandler(getActivity(), getArguments()
                            .getInt(BUNDLE_KEY_SELLER_ID)));
                    mBinding.setSellerID(getArguments()
                            .getInt(BUNDLE_KEY_SELLER_ID));
                    mBinding.executePendingBindings();
                    mBinding.progressBar.setVisibility(View.GONE);
                    /*Loading Banner Data*/
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {

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
}
