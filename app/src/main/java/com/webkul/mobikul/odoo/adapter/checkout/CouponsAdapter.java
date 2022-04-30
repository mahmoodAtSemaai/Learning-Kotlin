package com.webkul.mobikul.odoo.adapter.checkout;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemCouponBinding;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "CouponsAdapter";

    private final Context context;
    private final int NO_POSITION = -1;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    private int listSize = 8;


    public CouponsAdapter(Context context) {
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, position);
    }

    private void bindData(ViewHolder holder, int position) {
        if (holder.binding != null) {
            if (position == selectedPosition)
                holder.setSelection();
            else
                holder.removeSelection();
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return listSize;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCouponBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(v -> {
                selectedPosition = getAdapterPosition();
                if (lastSelectedPosition == NO_POSITION)
                    lastSelectedPosition = selectedPosition;
                else {
                    notifyItemChanged(lastSelectedPosition);
                    lastSelectedPosition = selectedPosition;
                }
                notifyItemChanged(selectedPosition);
            });
        }

        public void setSelection() {
            binding.clCouponsRootView.setBackgroundResource(R.drawable.rectangle_orange_border_6dp);
        }

        public void removeSelection() {
            binding.clCouponsRootView.setBackgroundResource(R.drawable.rectangle_grey_border_4dp);
        }
    }
}
