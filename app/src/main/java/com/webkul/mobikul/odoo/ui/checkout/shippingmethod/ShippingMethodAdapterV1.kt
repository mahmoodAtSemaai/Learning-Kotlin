package com.webkul.mobikul.odoo.ui.checkout.shippingmethod

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.ShippingMethodEntity
import com.webkul.mobikul.odoo.databinding.ItemShippingMethodLayoutBinding
import com.webkul.mobikul.odoo.databinding.ItemShippingMethodLayoutV1Binding
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod

class ShippingMethodAdapterV1(
    private val shippingMethods: List<ShippingMethodEntity>,
    private val shippingMethodSelectionListener: ShippingMethodSelectionListenerV1
) :
    RecyclerView.Adapter<ShippingMethodAdapterV1.ViewHolder>() {

    private val NO_POSITION = -1
    private var selectedPosition = -1
    private var lastSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shipping_method_layout_v1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ShippingMethodAdapterV1.ViewHolder, position: Int) {
        holder.bindData(shippingMethods.get(position))
        if (position == selectedPosition) {
            holder.setSelection()
            shippingMethodSelectionListener.updateShippingMethod(shippingMethods[position])
        } else
            holder.removeSelection()
    }

    override fun getItemCount(): Int {
        return shippingMethods.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemShippingMethodLayoutV1Binding = DataBindingUtil.bind(itemView)!!

        init {
            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                if (lastSelectedPosition == NO_POSITION)
                    lastSelectedPosition = selectedPosition
                else {
                    notifyItemChanged(lastSelectedPosition)
                    lastSelectedPosition = selectedPosition
                }
                //TODO: to be take up with seller wise checkout
                notifyItemChanged(selectedPosition)
            }
        }

        fun setSelection() {
            binding.ivShippingMethod.setImageResource(R.drawable.circular_radio_button_selected_state)
        }

        fun removeSelection() {
            binding.ivShippingMethod.setImageResource(R.drawable.circular_radio_button_unselected_state)
        }

        fun bindData(activeShippingMethod: ShippingMethodEntity) {
            binding.data = activeShippingMethod
        }
    }

    interface ShippingMethodSelectionListenerV1 {
        fun updateShippingMethod(shippingMethodEntity: ShippingMethodEntity)
    }


}