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


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "OrdersAdapter";

    private final Context context;
    private final List<OrderData> orderData;
    private final String sourceActivity;

    public OrdersAdapter(Context context, List<OrderData> orderDatas, String sourceActivity) {
        this.context = context;
        orderData = orderDatas;
        this.sourceActivity = sourceActivity;
    }

    public void updateOrderData(List<OrderData> orderDatas) {
        orderData.addAll(orderDatas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, position);
    }

    private void bindData(ViewHolder holder, int position) {
        final OrderData orderData = this.orderData.get(position);
        holder.binding.setData(orderData);
        holder.binding.setHandler(new OrderItemHandler(context, orderData, sourceActivity));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
