package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.activity.HomeActivity;
import com.webkul.mobikul.odoo.activity.SubCategoryActivity;
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl;
import com.webkul.mobikul.odoo.databinding.ItemDrawerStartCategoryBinding;
import com.webkul.mobikul.odoo.generated.callback.OnClickListener;
import com.webkul.mobikul.odoo.helper.CatalogHelper;
import com.webkul.mobikul.odoo.model.generic.CategoryData;

import java.util.List;

import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_ID;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_NAME;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_CATEGORY_OBJECT;
import static com.webkul.mobikul.odoo.constant.BundleConstant.BUNDLE_KEY_PARENT_CATEGORY;

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
public class NavDrawerCategoryStartRvAdapter extends RecyclerView.Adapter<NavDrawerCategoryStartRvAdapter.CategoryParentViewHolder> {
    private final Context mContext;
    private List<CategoryData> mCategoriesData;
    private final String mParentCategory;

    public NavDrawerCategoryStartRvAdapter(Context context, List<CategoryData> categoriesData, String parentCategory) {
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

//    @NonNull
//    @Override
//    public CategoryChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View childView = inflater.inflate(R.layout.item_drawer_start_subcategory, childViewGroup, false);
//        return new NavDrawerCategoryStartRvAdapter.CategoryChildViewHolder(childView);
//    }

    @Override
    public void onBindViewHolder(@NonNull CategoryParentViewHolder parentViewHolder, int parentPosition) {
        parentViewHolder.mBinding.setData(mCategoriesData.get(parentPosition));
        parentViewHolder.mBinding.getRoot().setOnClickListener((view) -> onClickParentCategoryItem(mCategoriesData.get(parentPosition)));
        parentViewHolder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mCategoriesData.size();
    }

    private void onClickParentCategoryItem(CategoryData parentCategoryData) {
        if (parentCategoryData.getChildren().isEmpty()) {
            AnalyticsImpl.INSTANCE.trackSubCategoryItemSelect(mParentCategory, parentCategoryData.getName(), parentCategoryData.getCategoryId());
            Intent intent = new Intent(mContext, CatalogProductActivity.class);
            intent.putExtra(BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE, CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY);
            intent.putExtra(BUNDLE_KEY_CATEGORY_ID, parentCategoryData.getCategoryId());
            intent.putExtra(BUNDLE_KEY_CATEGORY_NAME, parentCategoryData.getName());
            mContext.startActivity(intent);
        } else {
            AnalyticsImpl.INSTANCE.trackDynamicParentItemSelect(parentCategoryData.getName());
            Intent subCategoryIntent = new Intent(mContext, SubCategoryActivity.class);
            subCategoryIntent.putExtra(BUNDLE_KEY_PARENT_CATEGORY, parentCategoryData.getName());
            subCategoryIntent.putExtra(BUNDLE_KEY_CATEGORY_OBJECT, parentCategoryData);
            mContext.startActivity(subCategoryIntent);
        }
        if (mContext instanceof HomeActivity) {
            ((HomeActivity) mContext).mBinding.drawerLayout.closeDrawers();
        }

    }
//
//    @Override
//    public void onBindChildViewHolder(@NonNull CategoryChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull CategoryData childCategoryData) {
//        childViewHolder.mBinding.setData(childCategoryData);
//        childViewHolder.mBinding.setHandler(new NavDrawerStartSubCategoryHandler(mContext, childCategoryData));
//        childViewHolder.mBinding.executePendingBindings();
//    }


    static class CategoryParentViewHolder extends RecyclerView.ViewHolder {
        private ItemDrawerStartCategoryBinding mBinding;

        private CategoryParentViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

//        @Override
//        @UiThread
//        public void onClick(View v) {
//            super.onClick(v);
//            if (isExpanded()) {
//                mBinding.categoryNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_vector_arrow_down_grey600_18dp_wrapper, 0);
//            } else {
//                mBinding.categoryNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_vector_arrow_right_grey600_18dp_wrapper, 0);
//            }
//        }
    }

//    static class CategoryChildViewHolder extends ChildViewHolder {
//        private final ItemDrawerStartSubcategoryBinding mBinding;
//
//        private CategoryChildViewHolder(View itemView) {
//            super(itemView);
//            mBinding = DataBindingUtil.bind(itemView);
//        }
//    }
}