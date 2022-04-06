package com.webkul.mobikul.odoo.adapter.checkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemsOrderCheckoutBinding
import com.webkul.mobikul.odoo.model.customer.order.OrderItem

class CheckoutProductsAdapter(
    val context: Context,
    val orderItems: List<OrderItem>
) : RecyclerView.Adapter<CheckoutProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.items_order_checkout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ViewHolder, position: Int) {
        holder.bindData(orderItems.get(position))
    }

    override fun getItemCount() = orderItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemsOrderCheckoutBinding = DataBindingUtil.bind(itemView)!!

        fun bindData(orderItem: OrderItem) {
            binding.data = orderItem
        }
    }
}