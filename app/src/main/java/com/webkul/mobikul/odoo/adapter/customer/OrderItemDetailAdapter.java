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

public class OrderItemDetailAdapter extends RecyclerView.Adapter<OrderItemDetailAdapter.ViewHolder> {
    private final Context context;
    private final List<OrderItem> orderItems;

    public OrderItemDetailAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_order_product_info, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, position);
    }

    private void bindData(ViewHolder holder, int position) {
        final OrderItem orderItem = orderItems.get(position);
        holder.binding.setData(orderItem);
        holder.binding.setHandler(new OrderProductInfoItemHandler(context, orderItem));
/*
        holder.mBinding.executePendingBindings();
*/
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderProductInfoBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
