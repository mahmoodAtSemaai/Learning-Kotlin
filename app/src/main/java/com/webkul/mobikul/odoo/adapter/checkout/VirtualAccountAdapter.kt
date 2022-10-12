package com.webkul.mobikul.odoo.adapter.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemVaLayoutBinding
import com.webkul.mobikul.odoo.model.payments.PaymentAcquirerMethod
import java.util.ArrayList

class VirtualAccountAdapter(
    val context: Context,
    val virtualAccountList: MutableList<PaymentAcquirerMethod>,
    val paymentAcquirerFragmentListener: onVirtualAccountOptionSelected
) :
    RecyclerView.Adapter<VirtualAccountAdapter.ViewHolder>() {

    private val NO_POSITION = -1
    private var selectedPosition = -1
    private var lastSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_va_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: VirtualAccountAdapter.ViewHolder, position: Int) {
        holder.bindData(virtualAccountList.get(position))
        if (position == selectedPosition) {
            holder.setSelection()
            paymentAcquirerFragmentListener.updatePaymentOption(virtualAccountList.get(position))
        } else
            holder.removeSelection()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return virtualAccountList.size
    }

    fun resetAdapter() {
        selectedPosition = -1
        lastSelectedPosition = -1
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemVaLayoutBinding: ItemVaLayoutBinding = DataBindingUtil.bind(itemView)!!

        init {
            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                if (lastSelectedPosition == NO_POSITION)
                    lastSelectedPosition = selectedPosition
                else {
                    notifyItemChanged(lastSelectedPosition)
                    lastSelectedPosition = selectedPosition
                }
                notifyItemChanged(selectedPosition)
            }
        }

        fun bindData(paymentAcquirerMethod: PaymentAcquirerMethod) {
            itemVaLayoutBinding.data = paymentAcquirerMethod
        }


        fun setSelection() {
            itemVaLayoutBinding.ivOption.setImageResource(R.drawable.circular_radio_button_selected_state)
        }

        fun removeSelection() {
            itemVaLayoutBinding.ivOption.setImageResource(R.drawable.circular_radio_button_unselected_state)
        }
    }

    interface onVirtualAccountOptionSelected {
        fun updatePaymentOption(paymentAcquirerMethod: PaymentAcquirerMethod)
    }


}