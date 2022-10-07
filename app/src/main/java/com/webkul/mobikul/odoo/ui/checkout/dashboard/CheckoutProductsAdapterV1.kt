package com.webkul.mobikul.odoo.ui.checkout.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.data.entity.OrderProductEntity
import com.webkul.mobikul.odoo.databinding.ItemsOrderCheckoutBinding
import com.webkul.mobikul.odoo.databinding.ItemsOrderCheckoutV1Binding
import com.webkul.mobikul.odoo.model.customer.order.OrderItem

class CheckoutProductsAdapterV1(
    private val orderItems: List<OrderProductEntity>
) : RecyclerView.Adapter<CheckoutProductsAdapterV1.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items_order_checkout_v1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindData(holder, position)
    }

    private fun bindData(holder: ViewHolder, position: Int) {
        holder.bindData(orderItems[position])
    }

    override fun getItemCount() = orderItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemsOrderCheckoutV1Binding = DataBindingUtil.bind(itemView)!!

        fun bindData(orderItem: OrderProductEntity) {
            binding.data = orderItem
        }
    }
}