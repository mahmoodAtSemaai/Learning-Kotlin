package com.webkul.mobikul.odoo.adapter.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemAlternativeProductBinding;
import com.webkul.mobikul.odoo.handler.product.AlternativeProductHandler;
import com.webkul.mobikul.odoo.model.generic.ProductData;

import java.util.ArrayList;

/**

 * Webkul Software.

 * @package Mobikul App

 * @Category Mobikul

 * @author Webkul <support@webkul.com>

 * @Copyright (c) Webkul Software Private Limited (https://webkul.com)

 * @license https://store.webkul.com/license.html ASL Licence

 * @link https://store.webkul.com/license.html

 */

public class AlternativeProductsRvAdapter extends RecyclerView.Adapter<AlternativeProductsRvAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "FeaturedCategoriesRvAda";
    private boolean isContainMultipleImage;
    private final Context mContext;
    private final ArrayList<ProductData> mProductData;

    public AlternativeProductsRvAdapter(Context context, ArrayList<ProductData> mProductData, boolean isContainMultipleImage) {
        mContext = context;
        this.mProductData = mProductData;
        this.isContainMultipleImage = isContainMultipleImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_alternative_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductData featuredCategotyData = mProductData.get(position);
        if (isContainMultipleImage && featuredCategotyData.getImages().size() > 0) {
            featuredCategotyData.setImage(featuredCategotyData.getImages().get(0));
        }
        holder.mBinding.setData(featuredCategotyData);
        holder.mBinding.setHandler(new AlternativeProductHandler(mContext));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProductData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAlternativeProductBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}