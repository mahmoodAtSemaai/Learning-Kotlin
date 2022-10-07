package com.webkul.mobikul.odoo.adapter.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemShippingMethodLayoutBinding
import com.webkul.mobikul.odoo.model.checkout.ActiveShippingMethod

class ShippingMethodAdapter(
    val context: Context,
    val shippingMethods: List<ActiveShippingMethod>,
    val shippingMethodSelectionListener: ShippingMethodSelectionListener
) :
    RecyclerView.Adapter<ShippingMethodAdapter.ViewHolder>() {

    private val NO_POSITION = -1
    private var selectedPosition = -1
    private var lastSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_shipping_method_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ShippingMethodAdapter.ViewHolder, position: Int) {
        holder.bindData(shippingMethods.get(position))
        if (position == selectedPosition) {
            holder.setSelection()
            shippingMethodSelectionListener.updateShippingMethod(shippingMethods.get(position))
        } else
            holder.removeSelection()
    }

    override fun getItemCount(): Int {
        return shippingMethods.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemShippingMethodLayoutBinding = DataBindingUtil.bind(itemView)!!

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

        fun setSelection() {
            binding.ivShippingMethod.setImageResource(R.drawable.circular_radio_button_selected_state)
        }

        fun removeSelection() {
            binding.ivShippingMethod.setImageResource(R.drawable.circular_radio_button_unselected_state)
        }

        fun bindData(activeShippingMethod: ActiveShippingMethod) {
            binding.data = activeShippingMethod
        }
    }

    interface ShippingMethodSelectionListener {
        fun updateShippingMethod(activeShippingMethod: ActiveShippingMethod)
    }


}
