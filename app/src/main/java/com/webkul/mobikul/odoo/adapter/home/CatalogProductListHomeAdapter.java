package com.webkul.mobikul.odoo.adapter.home;

import static com.webkul.mobikul.odoo.activity.CatalogProductActivity.VIEW_TYPE_GRID;
import static com.webkul.mobikul.odoo.activity.CatalogProductActivity.VIEW_TYPE_LIST;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemCatalogProductListHomeBinding;
import com.webkul.mobikul.odoo.databinding.ItemProductGridBinding;
import com.webkul.mobikul.odoo.handler.home.ProductHandler;
import com.webkul.mobikul.odoo.helper.AppSharedPref;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;
import java.util.List;

public class CatalogProductListHomeAdapter extends RecyclerView.Adapter<CatalogProductListHomeAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "CatalogProductListRvAda";
    private final Context mContext;
    private final List<ProductData> mProductDatas;
    public int VIEW_TYPE;
    public int VIEW_TYPE_BACK_TO_TOP = 3;

    public CatalogProductListHomeAdapter(Context context, @NonNull ArrayList<ProductData> productDatas, int viewTypeGrid) {
        mContext = context;
        mProductDatas = productDatas;
        VIEW_TYPE = viewTypeGrid;
        Log.d(TAG, "onCreateViewHolderAdapter: " + viewTypeGrid);
    }

    @Override
    public CatalogProductListHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Log.d(TAG, "onCreateViewHolderviewType: " + viewType);
        if (viewType == VIEW_TYPE_BACK_TO_TOP) {
//            if(((CatalogProductActivity) mContext).mBinding.productCatalogRv.getLayoutManager() instanceof GridLayoutManager && mProductDatas.size()<=9){
//                return null;
//            }
            return new CatalogProductListHomeAdapter.ViewHolder(inflater.inflate(R.layout.item_button_back_to_top, parent, false));
        }
        if (viewType == VIEW_TYPE) {
            return new CatalogProductListHomeAdapter.ViewHolder(inflater.inflate(R.layout.item_catalog_product_list_home, parent, false));
        } else {
            return new CatalogProductListHomeAdapter.ViewHolder(inflater.inflate(R.layout.item_product_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(CatalogProductListHomeAdapter.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: ");
        if (position < mProductDatas.size()) {
            final ProductData productData = mProductDatas.get(position);
//        productData.setContext(mContext);
            if (getItemViewType(position) == VIEW_TYPE) {
                ((ItemCatalogProductListHomeBinding) holder.mBinding).setData(productData);
                ((ItemCatalogProductListHomeBinding) holder.mBinding).setHandler(new ProductHandler(mContext, productData));
                ((ItemCatalogProductListHomeBinding) holder.mBinding).getHandler().setProductListBinding(((ItemCatalogProductListHomeBinding) holder.mBinding));
                ((ItemCatalogProductListHomeBinding) holder.mBinding).setWishlistEnabled(AppSharedPref.isAllowedWishlist(mContext));
                ((ItemCatalogProductListHomeBinding) holder.mBinding).setIsLoggedIn(AppSharedPref.isLoggedIn(mContext));
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
