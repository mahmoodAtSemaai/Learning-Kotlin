package com.webkul.mobikul.odoo.ui.checkout.address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemAddressCheckoutV1Binding
import com.webkul.mobikul.odoo.model.customer.address.AddressData

class CheckoutAddressRecyclerViewAdapterV1(
    val context: Context, val addresses: List<AddressData>,
    val addressClickListener: CheckoutAddressRecyclerViewHandlerV1.OnAddressClickListener,
    val shippingAddressSelectedListener: ShippingAddressSelectedListener
) : RecyclerView.Adapter<CheckoutAddressRecyclerViewAdapterV1.ViewHolder>() {


    private val NO_POSITION = -1
    var selectedPosition = -1
    var lastSelectedPosition = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_address_checkout_v1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ViewHolder, position: Int) {
        val addressData = addresses[position]
        if (holder.binding != null) {
            if (position == selectedPosition) {
                holder.setSelection();
                shippingAddressSelectedListener.updateShippingAddress(addresses.get(position));
            } else
                holder.removeSelection();
            holder.binding.data = addressData
            holder.binding.position = position
            holder.binding.handler =
                CheckoutAddressRecyclerViewHandlerV1(
                    context,
                    addressData,
                    addressClickListener
                )
            holder.binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = addresses.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemAddressCheckoutV1Binding = DataBindingUtil.bind(itemView)!!

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

        fun bindData(addressData: AddressData) {
            binding.data = addressData
        }


        fun setSelection() {
            binding.ivAddressSelection.setImageResource(R.drawable.circular_radio_button_selected_state)
        }

        fun removeSelection() {
            binding.ivAddressSelection.setImageResource(R.drawable.circular_radio_button_unselected_state)
        }

    }

    interface ShippingAddressSelectedListener {
        fun updateShippingAddress(addressData: AddressData)
    }


}