package com.webkul.mobikul.odoo.adapter.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemPaymentMethodLayoutBinding
import com.webkul.mobikul.odoo.model.checkout.EWalletModel

class EWalletAdapter(val context: Context, val list: ArrayList<EWalletModel>) :
    RecyclerView.Adapter<EWalletAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_payment_method_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount() = list.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(eWalletModel: EWalletModel) {
            binding?.apply {
                ivPaymentMethod.setImageDrawable(eWalletModel.logo)
                tvPaymentMethod.text = eWalletModel.eWalletHeading
            }
        }

        private val binding: ItemPaymentMethodLayoutBinding? = DataBindingUtil.bind(itemView)
    }
}