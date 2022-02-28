package com.webkul.mobikul.odoo.adapter.catalog;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.activity.CatalogProductActivity;
import com.webkul.mobikul.odoo.databinding.ItemCatalogProductListBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductGridBinding;
import com.webkul.mobikul.odoo.handler.home.ProductHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.List;

import static com.webkul.mobikul.odoo.activity.CatalogProductActivity.VIEW_TYPE_GRID;
import static com.webkul.mobikul.odoo.activity.CatalogProductActivity.VIEW_TYPE_LIST;

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
public class CatalogProductListRvAdapter extends RecyclerView.Adapter<CatalogProductListRvAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "CatalogProductListRvAda";
    private final Context mContext;
    private final List<ProductData> mProductDatas;
    public int VIEW_TYPE;
    public int VIEW_TYPE_BACK_TO_TOP = 3;

    public CatalogProductListRvAdapter(Context context, @NonNull List<ProductData> productDatas, int viewTypeGrid) {
        mContext = context;
        mProductDatas = productDatas;
        VIEW_TYPE = viewTypeGrid;
        Log.d(TAG, "onCreateViewHolderAdapter: " + viewTypeGrid);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Log.d(TAG, "onCreateViewHolderviewType: " + viewType);
        if (viewType == VIEW_TYPE_BACK_TO_TOP) {
//            if(((CatalogProductActivity) mContext).mBinding.productCatalogRv.getLayoutManager() instanceof GridLayoutManager && mProductDatas.size()<=9){
//                return null;
//            }
            return new ViewHolder(inflater.inflate(R.layout.item_button_back_to_top, parent, false));
        }
        if (viewType == VIEW_TYPE) {
            return new ViewHolder(inflater.inflate(R.layout.item_catalog_product_list, parent, false));
        } else {
            return new ViewHolder(inflater.inflate(R.layout.item_product_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CatalogProductListRvAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        if (position < mProductDatas.size()) {
            final ProductData productData = mProductDatas.get(position);
//        productData.setContext(mContext);
            if (getItemViewType(position) == VIEW_TYPE) {
                ((ItemCatalogProductListBinding) holder.mBinding).setData(productData);
                ((ItemCatalogProductListBinding) holder.mBinding).setHandler(new ProductHandler(mContext, productData));
                ((ItemCatalogProductListBinding) holder.mBinding).getHandler().setProductListBinding(((ItemCatalogProductListBinding) holder.mBinding));
                ((ItemCatalogProductListBinding) holder.mBinding).setWishlistEnabled(AppSharedPref.isAllowedWishlist(mContext));
                ((ItemCatalogProductListBinding) holder.mBinding).setIsLoggedIn(AppSharedPref.isLoggedIn(mContext));
            } else {
                ((ItemProductGridBinding) holder.mBinding).setData(productData);
                ((ItemProductGridBinding) holder.mBinding).setIsLoggedIn(AppSharedPref.isLoggedIn(mContext));
                ((ItemProductGridBinding) holder.mBinding).setWishlistEnabled(AppSharedPref.isAllowedWishlist(mContext));
                ((ItemProductGridBinding) holder.mBinding).setHandler(new ProductHandler(mContext, productData));
                ((ItemProductGridBinding) holder.mBinding).getHandler().setProductDefaultBinding(((ItemProductGridBinding) holder.mBinding));
            }
            holder.mBinding.executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean isLinearLayoutManager = ((CatalogProductActivity) mContext).mBinding.productCatalogRv.getLayoutManager() instanceof LinearLayoutManager;
        Log.i(TAG, "getItemViewType: " + AppSharedPref.isGridview(mContext));
        if (position == mProductDatas.size()) {
            Log.i(TAG, "getItemViewType: buttonToTop");
            return VIEW_TYPE_BACK_TO_TOP;
        }
        if (AppSharedPref.isGridview(mContext)) {
            return VIEW_TYPE_GRID;
        } else {
            return VIEW_TYPE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + mProductDatas.size());
        if (mProductDatas.size() <= 9 && AppSharedPref.isGridview(mContext))
            return mProductDatas.size();
        if (mProductDatas.size() <= 5)
            return mProductDatas.size();
        return mProductDatas.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);

        }
    }
}
