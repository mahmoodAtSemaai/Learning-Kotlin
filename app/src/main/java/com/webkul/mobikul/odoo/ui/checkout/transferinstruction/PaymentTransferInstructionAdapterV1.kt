package com.webkul.mobikul.odoo.ui.checkout.transferinstruction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.core.extension.makeGone
import com.webkul.mobikul.odoo.core.extension.makeVisible
import com.webkul.mobikul.odoo.data.entity.TransferInstructionEntity
import com.webkul.mobikul.odoo.databinding.ItemPaymentTransferInstructionBinding

class PaymentTransferInstructionAdapterV1(
        val context: Context,
        val list: List<TransferInstructionEntity>,
        val listeners: TransferInstructionListeners
) : RecyclerView.Adapter<PaymentTransferInstructionAdapterV1.ViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): PaymentTransferInstructionAdapterV1.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_payment_transfer_instruction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)

        setClickListener(holder, position)
    }

    private fun bindData(holder: PaymentTransferInstructionAdapterV1.ViewHolder, position: Int) {

        val transferInstructionEntity = list[position]

        holder.binding.apply {
            tvMobileBankingHeading.text = transferInstructionEntity.heading
            tvPaymentTransferInstruction.text = transferInstructionEntity.instruction

            if (!transferInstructionEntity.isInstructionVisible) {
                tvPaymentTransferInstruction.makeGone()
                ivIcon.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_down_arrow_grey,
                        null
                )
            } else {
                tvPaymentTransferInstruction.makeVisible()
                ivIcon.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_up_arrow_grey,
                        null
                )
            }
            TransitionManager.beginDelayedTransition(holder.binding.clBase)
        }

    }

    private fun setClickListener(holder: PaymentTransferInstructionAdapterV1.ViewHolder, position: Int){
        holder.binding.root.setOnClickListener {
            listeners.onChildInstructionTextClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemPaymentTransferInstructionBinding = DataBindingUtil.bind(this.itemView)!!
    }

}