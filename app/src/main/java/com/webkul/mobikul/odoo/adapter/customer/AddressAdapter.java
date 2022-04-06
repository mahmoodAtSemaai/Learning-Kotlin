package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemAdditionalAddressBinding;
import com.webkul.mobikul.odoo.handler.customer.AddressRecyclerViewHandler;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "AddressAdapter";

    private final Context context;
    private final List<AddressData> addresses;
    private AddressRecyclerViewHandler.OnAdditionalAddressDeletedListener mOnAdditionalAddressDeletedListener;


    public AddressAdapter(Context context, List<AddressData> additionalAddress,
                          AddressRecyclerViewHandler.OnAdditionalAddressDeletedListener onAdditionalAddressDeletedListener) {
        this.context = context;
        addresses = additionalAddress;
        mOnAdditionalAddressDeletedListener = onAdditionalAddressDeletedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_additional_address, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindData(holder, position);
    }

    private void bindData(ViewHolder holder, int position) {
        final AddressData additionalAddress = addresses.get(position);
        if (holder.binding != null) {
            holder.binding.setData(additionalAddress);
            holder.binding.setPosition(position);
            holder.binding.setHandler(new AddressRecyclerViewHandler(context, additionalAddress, mOnAdditionalAddressDeletedListener));
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdditionalAddressBinding binding;

        private ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
