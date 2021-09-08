package com.webkul.mobikul.odoo.adapter.catalog;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderBinding;
import com.webkul.mobikul.odoo.handler.customer.OrderItemHandler;
import com.webkul.mobikul.odoo.model.customer.order.OrderData;

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

public class OrderRvAdapter extends RecyclerView.Adapter<OrderRvAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderRvAdapter";

    private final Context mContext;
    private final List<OrderData> mOrderDatas;

    public OrderRvAdapter(Context context, List<OrderData> orderDatas) {
        mContext = context;
        mOrderDatas = orderDatas;
    }

    public void updateOrderData(List<OrderData> orderDatas) {
        mOrderDatas.addAll(orderDatas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderData orderData = mOrderDatas.get(position);
        holder.mBinding.setData(orderData);
        holder.mBinding.setHandler(new OrderItemHandler(mContext, orderData));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mOrderDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
