package com.webkul.mobikul.odoo.adapter.customer;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemAdditionalAddressBinding;
import com.webkul.mobikul.odoo.handler.customer.AdditionalAddressRvHandler;
import com.webkul.mobikul.odoo.model.customer.address.AddressData;

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
public class AdditionalAddressRVAdapter extends RecyclerView.Adapter<AdditionalAddressRVAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "AdditionalAddressRVAdap";

    private final Context mContext;
    private final List<AddressData> mAdditionalAddress;
    private AdditionalAddressRvHandler.OnAdditionalAddressDeletedListener mOnAdditionalAddressDeletedListener;

    public AdditionalAddressRVAdapter(Context context, List<AddressData> additionalAddress, AdditionalAddressRvHandler.OnAdditionalAddressDeletedListener onAdditionalAddressDeletedListener) {
        mContext = context;
        mAdditionalAddress = additionalAddress;
        mOnAdditionalAddressDeletedListener = onAdditionalAddressDeletedListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_additional_address, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AddressData additionalAddress = mAdditionalAddress.get(position);
        if (holder.mBinding != null) {
            holder.mBinding.setData(additionalAddress);
            holder.mBinding.setPosition(position);
            holder.mBinding.setHandler(new AdditionalAddressRvHandler(mContext, additionalAddress, mOnAdditionalAddressDeletedListener));
            holder.mBinding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mAdditionalAddress.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdditionalAddressBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
