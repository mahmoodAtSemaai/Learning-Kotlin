package com.webkul.mobikul.odoo.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.ItemCategoryProductsBinding
import com.webkul.mobikul.odoo.model.generic.ProductData

class CategoryProductListAdapter(
    val context: Context,
    val productList: ArrayList<ProductData>
) :
    RecyclerView.Adapter<CategoryProductListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_category_products, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.data = productList[position]
    }


    override fun getItemCount(): Int = productList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemCategoryProductsBinding = DataBindingUtil.bind(itemView)!!
    }


}