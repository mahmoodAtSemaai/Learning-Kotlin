package com.webkul.mobikul.odoo.ui.checkout.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity;
import com.webkul.mobikul.odoo.databinding.ItemOrderProductInfoBinding;
import com.webkul.mobikul.odoo.databinding.ItemOrderProductInfoV1Binding;
import com.webkul.mobikul.odoo.handler.customer.OrderProductInfoItemHandler;
import com.webkul.mobikul.odoo.model.customer.order.OrderItem;

import java.util.List;

public class OrderItemDetailAdapterV1 extends RecyclerView.Adapter<OrderItemDetailAdapterV1.ViewHolder> {
    private final Context context;
    private final List<OrderProductEntity> orderItems;

    public OrderItemDetailAdapterV1(Context context, List<OrderProductEntity> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_order_product_info_v1, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, position);
    }

    private void bindData(ViewHolder holder, int position) {
        final OrderProductEntity orderItem = orderItems.get(position);
        holder.binding.setData(orderItem);
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderProductInfoV1Binding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
