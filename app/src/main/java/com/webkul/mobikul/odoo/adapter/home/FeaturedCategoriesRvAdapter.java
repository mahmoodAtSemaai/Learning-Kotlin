package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemFeaturedCategoryBinding;
import com.webkul.mobikul.odoo.handler.home.FeaturedCategoryHandler;
import com.webkul.mobikul.odoo.model.generic.FeaturedCategoryData;

import java.util.List;
/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class FeaturedCategoriesRvAdapter extends RecyclerView.Adapter<FeaturedCategoriesRvAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "FeaturedCategoriesRvAda";


    private final Context mContext;
    private final List<FeaturedCategoryData> mFeaturedCategoryDatas;

    public FeaturedCategoriesRvAdapter(Context context, List<FeaturedCategoryData> featuredCategotyDatas) {
        mContext = context;
        mFeaturedCategoryDatas = featuredCategotyDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_featured_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeaturedCategoryData featuredCategotyData = mFeaturedCategoryDatas.get(position);
        holder.mBinding.setData(featuredCategotyData);
        holder.mBinding.setHandler(new FeaturedCategoryHandler(mContext, featuredCategotyData));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mFeaturedCategoryDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemFeaturedCategoryBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}