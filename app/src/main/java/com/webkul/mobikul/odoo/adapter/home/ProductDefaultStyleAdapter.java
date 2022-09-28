package com.webkul.mobikul.odoo.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemProductGridBinding;
import com.webkul.mobikul.odoo.handler.home.ProductHandler;
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

public class ProductDefaultStyleAdapter extends RecyclerView.Adapter<ProductDefaultStyleAdapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "ProductDefaultStyleRvAdapter";
    private final Context mContext;
    private ArrayList<ProductData> mProductDatas;
    private String mSliderMode;

    public ProductDefaultStyleAdapter(Context context, ArrayList<ProductData> productDatas, String sliderMode) {
        mContext = context;
        mProductDatas = productDatas;
        mSliderMode = sliderMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_product_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductData productData = mProductDatas.get(position);
        //TODO -> need to check this
//        holder.mBinding.setData(productData);
        holder.mBinding.setSliderMode(mSliderMode);
        //TODO -> need to check this
//        holder.mBinding.setHandler(new ProductHandler(mContext, productData));
        holder.mBinding.getHandler().setProductDefaultBinding(holder.mBinding);
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProductDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProductGridBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
