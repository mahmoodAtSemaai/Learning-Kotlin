package com.webkul.mobikul.odoo.adapter.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.activity.CatalogProductActivity
import com.webkul.mobikul.odoo.activity.HomeActivity
import com.webkul.mobikul.odoo.activity.SubCategoryActivity
import com.webkul.mobikul.odoo.analytics.AnalyticsImpl
import com.webkul.mobikul.odoo.constant.BundleConstant
import com.webkul.mobikul.odoo.databinding.ItemDrawerChildCategoryBinding
import com.webkul.mobikul.odoo.helper.CatalogHelper
import com.webkul.mobikul.odoo.model.generic.CategoryData


class NewChildDrawerLayoutAdapter(
    private val mContext: Context,
    private val mCategoriesData: List<CategoryData>,
    private val mParentCategory: String
) :
    RecyclerView.Adapter<NewChildDrawerLayoutAdapter.CategoryParentViewHolder>() {

    override fun onCreateViewHolder(
        parentViewGroup: ViewGroup,
        viewType: Int
    ): CategoryParentViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val parentView: View =
            inflater.inflate(R.layout.item_drawer_child_category, parentViewGroup, false)
        return CategoryParentViewHolder(parentView)
    }

    override fun onBindViewHolder(parentViewHolder: CategoryParentViewHolder, parentPosition: Int) {
        parentViewHolder.mBinding?.data = mCategoriesData[parentPosition]
        parentViewHolder.mBinding?.root!!.setOnClickListener {
            onClickParentCategoryItem(
                mCategoriesData[parentPosition]
            )
        }
        parentViewHolder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mCategoriesData.size
    }

    private fun onClickParentCategoryItem(parentCategoryData: CategoryData) {
        if (parentCategoryData.children.isEmpty()) {
            AnalyticsImpl.trackSubCategoryItemSelect(
                mParentCategory,
                parentCategoryData.name,
                parentCategoryData.categoryId
            )
            val intent = Intent(mContext, CatalogProductActivity::class.java)
            intent.putExtra(
                BundleConstant.BUNDLE_KEY_CATALOG_PRODUCT_REQ_TYPE,
                CatalogHelper.CatalogProductRequestType.GENERAL_CATEGORY
            )
            intent.putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_ID, parentCategoryData.categoryId)
            intent.putExtra(BundleConstant.BUNDLE_KEY_CATEGORY_NAME, parentCategoryData.name)
            mContext.startActivity(intent)
        } else {
            AnalyticsImpl.trackDynamicParentItemSelect(parentCategoryData.name)
            val subCategoryIntent = Intent(mContext, SubCategoryActivity::class.java)
            subCategoryIntent.putExtra(
                BundleConstant.BUNDLE_KEY_PARENT_CATEGORY,
                parentCategoryData.name
            )
            subCategoryIntent.putExtra(
                BundleConstant.BUNDLE_KEY_CATEGORY_OBJECT,
                parentCategoryData
            )
            mContext.startActivity(subCategoryIntent)
        }
        if (mContext is HomeActivity) {
            mContext.mBinding.drawerLayout.closeDrawers()
        }
    }


    class CategoryParentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val mBinding: ItemDrawerChildCategoryBinding? = DataBindingUtil.bind(itemView)

    }
}