package com.webkul.mobikul.odoo.adapter.checkout;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderReviewProductBinding;
import com.webkul.mobikul.odoo.model.cart.BagItemData;

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

public class OrderReviewProductAdapter extends RecyclerView.Adapter<OrderReviewProductAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderReviewProductAdapt";
    private final Context mContext;
    private List<BagItemData> mBagItemDatas;

    public OrderReviewProductAdapter(Context context, List<BagItemData> bagItemDatas) {
        mContext = context;
        mBagItemDatas = bagItemDatas;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_order_review_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BagItemData bagItemData = mBagItemDatas.get(position);
        holder.mBinding.setData(bagItemData);
    }

    @Override
    public int getItemCount() {
        return mBagItemDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderReviewProductBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

}
