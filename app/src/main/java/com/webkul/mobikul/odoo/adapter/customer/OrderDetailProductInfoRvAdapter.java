package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderProductInfoBinding;
import com.webkul.mobikul.odoo.handler.customer.OrderProductInfoItemHandler;
import com.webkul.mobikul.odoo.model.customer.order.OrderItem;

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
public class OrderDetailProductInfoRvAdapter extends RecyclerView.Adapter<OrderDetailProductInfoRvAdapter.ViewHolder> {
    private final Context mContext;
    private final List<OrderItem> mOrderItems;

    public OrderDetailProductInfoRvAdapter(Context context, List<OrderItem> orderItems) {
        mContext = context;
        mOrderItems = orderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_order_product_info, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderItem orderItem = mOrderItems.get(position);
        holder.mBinding.setData(orderItem);
        holder.mBinding.setHandler(new OrderProductInfoItemHandler(mContext, orderItem));
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mOrderItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderProductInfoBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
