package com.webkul.mobikul.odoo.adapter.catalog;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemOrderBinding;
import com.webkul.mobikul.odoo.handler.customer.OrderItemHandler;
import com.webkul.mobikul.odoo.model.customer.order.OrderData;

import java.util.List;

public class OrderRvAdapter extends RecyclerView.Adapter<OrderRvAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "OrderRvAdapter";

    private final Context mContext;
    private final List<OrderData> mOrderDatas;
    private final String mSourceActivity;

    public OrderRvAdapter(Context context, List<OrderData> orderDatas, String sourceActivity) {
        mContext = context;
        mOrderDatas = orderDatas;
        mSourceActivity = sourceActivity;
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
        holder.mBinding.setHandler(new OrderItemHandler(mContext, orderData, mSourceActivity));
        holder.mBinding.executePendingBindings();

        holder.mBinding.orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavController navController = Navigation.findNavController(view);

                Fragment fragment = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putInt("orderid" , Integer.parseInt(mOrderDatas.get(holder.getAdapterPosition()).getId()));
                fragment.setArguments(bundle);
                navController.navigate(R.id.action_orderListFragment_to_orderFragment , bundle);
            }
        });
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
