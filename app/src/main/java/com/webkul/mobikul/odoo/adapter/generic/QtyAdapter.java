package com.webkul.mobikul.odoo.adapter.generic;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webkul.mobikul.odoo.BuildConfig;
import com.webkul.mobikul.odoo.R;
import com.webkul.mobikul.odoo.databinding.ItemQtyBinding;
import com.webkul.mobikul.odoo.dialog_frag.ChangeQtyDialogFragment;
import com.webkul.mobikul.odoo.handler.generic.QtyAdapterHandler;

import java.util.ArrayList;
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

public class QtyAdapter extends RecyclerView.Adapter<QtyAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = "QtyAdapter";

    private final Context mContext;
    private ChangeQtyDialogFragment.OnQtyChangeListener mOnQtyChangeListener;
    private List<String> mQtys;
    private int mCurrentQty;

    public QtyAdapter(Context context, ChangeQtyDialogFragment.OnQtyChangeListener onQtyChangeListener, int currentQty) {
        mContext = context;
        mOnQtyChangeListener = onQtyChangeListener;
         /*init qty*/
        mQtys = new ArrayList<>();
        for (int i = 0; i < BuildConfig.DEFAULT_MAX_QTY; i++) {
            mQtys.add(String.valueOf(i + 1));
        }
        mQtys.add(mContext.getString(R.string.more));
        mCurrentQty = currentQty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = inflater.inflate(R.layout.item_qty, parent, false);
        return new ViewHolder(contactView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setValue(mQtys.get(position));
        if (mQtys.get(position).equals(String.valueOf(mCurrentQty))) {
            holder.mBinding.qtyTv.setChecked(true);
        } else {
            holder.mBinding.qtyTv.setChecked(false);
        }
        holder.mBinding.setHandler(new QtyAdapterHandler(mContext, position, mCurrentQty, mOnQtyChangeListener));
    }

    @Override
    public int getItemCount() {
        return mQtys.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemQtyBinding mBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
