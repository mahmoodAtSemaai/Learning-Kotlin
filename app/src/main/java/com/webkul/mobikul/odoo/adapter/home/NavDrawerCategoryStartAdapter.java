package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.data.entity.ProductCategoryEntity;
import com.webkul.mobikul.odoo.databinding.ItemDrawerStartCategoryBinding;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.model.generic.CategoryData;

import java.util.List;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;

/**
 * Webkul Software.
 *
 * @author Webkul <support@webkul.com>
 * @package Mobikul App
 * @Category Mobikul
 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)
 * @license https://store.webkul.com/license.html ASL Licence
 * @link https://store.webkul.com/license.html
 */
public class NavDrawerCategoryStartAdapter extends RecyclerView.Adapter<NavDrawerCategoryStartAdapter.CategoryParentViewHolder> {
    private final Context mContext;
    private List<ProductCategoryEntity> mCategoriesData;
    private final String mParentCategory;

    public NavDrawerCategoryStartAdapter(Context context, List<ProductCategoryEntity> categoriesData, String parentCategory) {
        mContext = context;
        mCategoriesData = categoriesData;
        mParentCategory = parentCategory;
    }

    @NonNull
    @Override
    public CategoryParentViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View parentView = inflater.inflate(R.layout.item_drawer_start_category, parentViewGroup, false);
        return new CategoryParentViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryParentViewHolder parentViewHolder, int parentPosition) {
        parentViewHolder.mBinding.setData(mCategoriesData.get(parentPosition));
        parentViewHolder.mBinding.getRoot().setOnClickListener((view) -> onClickParentCategoryItem(parentViewHolder, mCategoriesData.get(parentPosition)));
        parentViewHolder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mCategoriesData.size();
    }

    private void onClickParentCategoryItem(@NonNull CategoryParentViewHolder parentViewHolder, ProductCategoryEntity parentCategoryData) {
        if (parentCategoryData.getChildren().isEmpty()) {
            AnalyticsImpl.INSTANCE.trackSubCategoryItemSelect(mParentCategory, parentCategoryData.getName(), String.valueOf(parentCategoryData.getCategoryId()));
            Intent intent = new Intent(mContext, CatalogProductActivity.class);
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
            intent.putExtra(BUNDLE_KEY_CATEGORY_ID, parentCategoryData.getCategoryId());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, parentCategoryData.getName());
            mContext.startActivity(intent);
        } else {
            if (parentViewHolder.mBinding.childRecyclerview.isShown()) {
                parentViewHolder.mBinding.childRecyclerview.setVisibility(View.GONE);
                changeDrawable(parentViewHolder, R.drawable.ic_baseline_keyboard_arrow_down_24);
            } else {
                parentViewHolder.mBinding.childRecyclerview.setVisibility(View.VISIBLE);
                changeDrawable(parentViewHolder, R.drawable.ic_baseline_keyboard_arrow_up_24);
            }

            List<ProductCategoryEntity> data = parentCategoryData.getChildren();
            String name = parentCategoryData.getName();
            NewChildDrawerLayoutAdapter adapter = new NewChildDrawerLayoutAdapter(parentViewHolder.itemView.getContext(), data, name);
            parentViewHolder.mBinding.childRecyclerview.setLayoutManager(new LinearLayoutManager(parentViewHolder.itemView.getContext()));
            parentViewHolder.mBinding.childRecyclerview.setHasFixedSize(true);
            parentViewHolder.mBinding.childRecyclerview.setAdapter(adapter);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(parentViewHolder.mBinding.childRecyclerview);
            }

        }
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
        }

    }

    private void changeDrawable(@NonNull CategoryParentViewHolder parentViewHolder, Integer drawable) {
        parentViewHolder.mBinding.categoryNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }


    static class CategoryParentViewHolder extends RecyclerView.ViewHolder {
        private ItemDrawerStartCategoryBinding mBinding;

        private CategoryParentViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }


}