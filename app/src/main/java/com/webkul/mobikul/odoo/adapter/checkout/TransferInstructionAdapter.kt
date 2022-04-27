package com.webkul.mobikul.odoo.adapter.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.PaymentInstructionBinding
import com.webkul.mobikul.odoo.model.checkout.TransferInstruction

class TransferInstructionAdapter(
    val context: Context,
    val list: List<TransferInstruction>
) : RecyclerView.Adapter<TransferInstructionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransferInstructionAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.payment_instruction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: TransferInstructionAdapter.ViewHolder, position: Int) {
        holder.binding.data = list.get(position)
        holder.binding.root.setOnClickListener {
            if (holder.binding.tvPaymentTransferInstruction.visibility == View.VISIBLE) {
                holder.binding.tvPaymentTransferInstruction.visibility = View.GONE
                holder.binding.ivIcon.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_down_arrow_grey,
                    null
                )
            } else {
                holder.binding.tvPaymentTransferInstruction.visibility = View.VISIBLE
                holder.binding.ivIcon.background = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_up_arrow_grey,
                    null
                )
            }
            TransitionManager.beginDelayedTransition(holder.binding.base)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: PaymentInstructionBinding = DataBindingUtil.bind(this.itemView)!!
    }

}