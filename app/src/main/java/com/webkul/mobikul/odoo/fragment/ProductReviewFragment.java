package com.webkul.mobikul.odoo.fragment;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.SignInSignUpActivity;
import com.webkul.mobikul.odoo.adapter.product.ProductReviewInfoRvAdapter;
import com.webkul.mobikul.odoo.connection.ApiConnection;
import com.webkul.mobikul.odoo.connection.CustomObserver;
import com.webkul.mobikul.odoo.databinding.FragmentProductReviewBinding;
import com.webkul.mobikul.odoo.handler.product.ProductReviewHandler;
import com.webkul.mobikul.odoo.helper.AlertDialogHelper;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.product.ProductReviewResponse;
import com.webkul.mobikul.odoo.model.product.Review;
import com.webkul.mobikul.odoo.model.request.ProductReviewRequest;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CALLING_ACTIVITY;


/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class ProductReviewFragment extends BaseFragment {
    private static final String TAG = "ProductReviewFragment";
    private static final String TEMPLATE_ID = "templateId";
    public FragmentProductReviewBinding mBinding;
    private boolean isFirstCall = true;
    private ProductReviewInfoRvAdapter mProductReviewInfoRvAdapter;
    private List<Review> reviewList = new ArrayList<>();

    public static ProductReviewFragment newInstance(String templateId) {
        ProductReviewFragment fragment = new ProductReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEMPLATE_ID, templateId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_review, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstCall)
            callApi();
    }


    private void callApi() {
        ApiConnection.getProductReviews(getActivity(), new ProductReviewRequest(getArguments().getString(TEMPLATE_ID))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomObserver<ProductReviewResponse>(getActivity()) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onNext(@NonNull ProductReviewResponse response) {
                        super.onNext(response);
                        if (response.isAccessDenied()) {
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
                            mBinding.setData(response);
                            mBinding.setHandler(new ProductReviewHandler(getActivity(), getArguments().getString(TEMPLATE_ID)));
                            mBinding.executePendingBindings();
                            if (reviewList.size() > 0) {
                                reviewList.clear();
                            }
                            reviewList.addAll(response.getProductReviews());
                            if (isFirstCall) {
                                DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);
                                mBinding.reviewRv.addItemDecoration(dividerItemDecorationHorizontal);
                                mProductReviewInfoRvAdapter = new ProductReviewInfoRvAdapter(getContext(), reviewList);
                                mBinding.reviewRv.setAdapter(mProductReviewInfoRvAdapter);
                            } else {
                                mProductReviewInfoRvAdapter.notifyDataSetChanged();
                            }
                            isFirstCall = false;
                        }
                    }
                    @Override
                    public void onComplete() {

                    }
                });
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
